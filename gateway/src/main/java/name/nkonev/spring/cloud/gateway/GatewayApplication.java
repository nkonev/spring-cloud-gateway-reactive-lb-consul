package name.nkonev.spring.cloud.gateway;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.DispatcherHandler;
import reactor.core.publisher.Mono;

// while true; do curl --location --request POST 'http://localhost:8001/test/' --header 'Content-Type: application/json' --data-raw '{}';  done
// while true; do curl -v --location --request POST 'http://localhost:8001/test/' --header 'Content-Type: application/json' --data-raw '{}';  done
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
                .filters(f -> {
                            GatewayFilterSpec s = f.stripPrefix(1);

                            ModifyRequestBodyGatewayFilterFactory2.Config cc = new ModifyRequestBodyGatewayFilterFactory2.Config();
                            cc.setInClass(String.class);
                            cc.setOutClass(String.class);
                            cc.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            cc.setRewriteFunction((exchange, s_) -> {
                                byte[] body = new byte[10 * 1024 * 1024];
                                return Mono.just(new String(body));
                            });
                            ModifyRequestBodyGatewayFilterFactory2 mff = new ModifyRequestBodyGatewayFilterFactory2();
                            GatewayFilter mrf = mff.apply(cc);
                            s.filter(mrf);
                            return s;
                }
                ).uri("http://localhost:8443")).build();

    }



}