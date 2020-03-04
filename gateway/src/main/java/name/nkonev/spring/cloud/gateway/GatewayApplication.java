package name.nkonev.spring.cloud.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

// curl http://localhost:8282/test-route?sleep=3
@SpringBootApplication
@RestController
public class GatewayApplication {

    public static final String TEST_CIRCUIT_BREAKER = "testCircuitBreaker";

    @Autowired
    private ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @PostConstruct
    public void pc(){
        reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> {
            TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.of(7, ChronoUnit.SECONDS)).build();
            Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration configuration = new Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(timeLimiterConfig).build();
            return configuration;
        });
    }

    @Bean
    public RouteLocator testRouteLocator(final RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("test Route", p -> p.path("/test-route")
                        .filters(
                                f -> f.rewritePath("/test-route", "/v1/test-route")
                                .circuitBreaker(c -> c.setName(TEST_CIRCUIT_BREAKER)
                                        .setFallbackUri("forward:/fallback/test")
                                ).retry(3)
                        )
                        .uri("lb://test-service")).build();
    }

    @RequestMapping("/fallback/test")
    public String fb() {
        return "fallback =(";
    }
}