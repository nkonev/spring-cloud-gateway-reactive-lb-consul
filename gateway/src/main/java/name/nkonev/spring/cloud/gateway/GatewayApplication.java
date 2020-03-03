package name.nkonev.spring.cloud.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerResilience4JFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.retry.Repeat;
import reactor.retry.Retry;

import java.net.URI;

@SpringBootApplication
@RestController
public class GatewayApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /*@Bean
    public RouteLocator testRouteLocator(final RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("test Route", p -> p.path("/test-route")
                        .filters(
                                f -> f.rewritePath("/test-route", "/v1/test-route")
                                .retry(3)
                                .circuitBreaker(c -> c.setName("testCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/test")
                                )
                        )
                        .uri("lb://test-service")).build();
    }*/


    static class WithFallbackRetryGatewayFilterFactory extends RetryGatewayFilterFactory {

        private final ObjectProvider<DispatcherHandler> dispatcherHandler;

        private static final Logger LOGGER = LoggerFactory.getLogger(WithFallbackRetryGatewayFilterFactory.class);

        public WithFallbackRetryGatewayFilterFactory(ObjectProvider<DispatcherHandler> objectProvider) {
            this.dispatcherHandler = objectProvider;
        }

        @Override
        public GatewayFilter apply(String routeId, Repeat<ServerWebExchange> repeat,
                                    Retry<ServerWebExchange> retry) {
            GatewayFilter partialFilter = super.apply(routeId, repeat, retry);
            return new GatewayFilter() {
                @Override
                public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .uri(URI.create("forward:/fallback/test")).build();

                    Mono<Void> originalMono = partialFilter.filter(exchange, chain);

                    Mono<Void> withFallbackMono = originalMono.onErrorResume(e -> {
                        LOGGER.error("error in RetryFilter, falling back");
                        return dispatcherHandler.getIfAvailable().handle(exchange.mutate().request(request).build());
                    });

                    return withFallbackMono;
                }
            };
        }

    }

    @Bean
    public RouteLocator testRouteLocator(final RouteLocatorBuilder routeLocatorBuilder,
                                         RewritePathGatewayFilterFactory rewritePathGatewayFilterFactory,
                                         ObjectProvider<DispatcherHandler> dispatcherHandlerObjectProvider) {
        return routeLocatorBuilder.routes().route("test Route", p -> p.path("/test-route")
                        .filters(f ->
                                {
                                    RewritePathGatewayFilterFactory.Config rewriteConfig = new RewritePathGatewayFilterFactory.Config();
                                    rewriteConfig.setRegexp("/test-route");
                                    rewriteConfig.setReplacement("/v1/test-route");
                                    GatewayFilter rewriteFilter = rewritePathGatewayFilterFactory.apply(rewriteConfig);
                                    f.filter(rewriteFilter, 0);

                                    RetryGatewayFilterFactory.RetryConfig retryConfig = new RetryGatewayFilterFactory.RetryConfig();
                                    retryConfig.setRetries(3);
                                    WithFallbackRetryGatewayFilterFactory rf = new WithFallbackRetryGatewayFilterFactory(dispatcherHandlerObjectProvider);
                                    GatewayFilter retryFilter = rf.apply(retryConfig);
                                    f.filter(retryFilter, 1);

                                    return f;
                                }
                        ).uri("lb://test-service")
        ).build();
    }




    /*@Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> customizer() {
        return reactiveResilience4JCircuitBreakerFactory -> {

            reactiveResilience4JCircuitBreakerFactory.addCircuitBreakerCustomizer(circuitBreaker -> {
                //circuitBreaker.
            }, TEST_CIRCUIT_BREAKER);

            /*reactiveResilience4JCircuitBreakerFactory.configure(resilience4JConfigBuilder -> {
                CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().ignoreExceptions(TimeoutException.class).build();
                resilience4JConfigBuilder.circuitBreakerConfig(circuitBreakerConfig);
            }, TEST_CIRCUIT_BREAKER);
            LOGGER.info("I am custmized");
        };
    }*/

    @RequestMapping("/fallback/test")
    public String fb() {
        return "fallback =(";
    }
}