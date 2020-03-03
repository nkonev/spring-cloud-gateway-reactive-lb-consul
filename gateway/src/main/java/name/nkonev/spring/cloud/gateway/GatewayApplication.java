package name.nkonev.spring.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator testRouteLocator(final RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("test Route", p -> p.path("/test-route")
                        .filters(
                                f -> f.rewritePath("/test-route", "/v1/test-route")
                                .circuitBreaker(c -> c.setName("testCircuitBreaker")
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