package name.nkonev.spring.cloud.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TestGlobalFilter2 implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ServerWebExchangeUtils.cacheRequestBody(exchange, (serverHttpRequest) -> {
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
