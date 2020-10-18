package name.nkonev.spring.cloud.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomGlobalFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final NettyDataBuffer dataBuffer = (NettyDataBuffer) exchange.getResponse().bufferFactory().allocateBuffer(100);
        LOGGER.trace("Make a leak");
        dataBuffer.getNativeBuffer().copy();
        return chain.filter(exchange);
    }
}
