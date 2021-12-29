package name.nkonev.spring.cloud.gateway;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class H2CAwareNettyRoutingFilter extends NettyRoutingFilter {

    public H2CAwareNettyRoutingFilter(HttpClient httpClient, ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider, HttpClientProperties properties) {
        super(httpClient, headersFiltersProvider, properties);
    }

    @Override
    protected HttpClient getHttpClient(Route route, ServerWebExchange exchange) {
        HttpClient httpClient = super.getHttpClient(route, exchange);

        boolean h2cRoute = ofNullable(route.getMetadata().get("h2c"))
                .map(o -> (Boolean)o)
                .orElse(false);
        if (h2cRoute) {
            // https://projectreactor.io/docs/netty/release/reference/index.html#_http2_2
            return httpClient.protocol(HttpProtocol.H2C);
        } else {
            return httpClient;
        }
    }

    @Override
    public int getOrder() {
        return super.getOrder() - 1;
    }

}
