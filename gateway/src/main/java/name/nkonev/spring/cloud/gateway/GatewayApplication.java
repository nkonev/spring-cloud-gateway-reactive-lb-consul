package name.nkonev.spring.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

// while true; do curl --location --request POST 'http://localhost:8001/test/' --header 'Content-Type: application/json' --data-raw '{}';  done
// -XX:MaxDirectMemorySize=200M
// https://dzone.com/articles/troubleshooting-problems-with-native-off-heap-memo
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes().route("test", r -> r.path("/test/**")
                .filters(f -> f.stripPrefix(1).modifyRequestBody(String.class, String.class, MediaType.APPLICATION_JSON_VALUE,
                        (exchange, s) -> {
                            byte[] body = new byte[10 * 1024 * 1024];
                            return Mono.just(new String(body));
                        }).filter(((exchange, chain) -> {
                    return chain.filter(exchange);
                }))).uri("http://localhost:8443")).build();

    }

}