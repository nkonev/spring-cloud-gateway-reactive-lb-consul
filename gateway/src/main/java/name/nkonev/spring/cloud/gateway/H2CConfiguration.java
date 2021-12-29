package name.nkonev.spring.cloud.gateway;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.embedded.NettyWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.netty.http.HttpProtocol;

// See also GatewayAutoConfiguration
@Configuration
public class H2CConfiguration {

    // https://projectreactor.io/docs/netty/release/reference/index.html#_http2
    @Bean
    public NettyWebServerFactoryCustomizer h2cServerCustomizer(Environment environment, ServerProperties serverProperties) {
        return new NettyWebServerFactoryCustomizer(environment, serverProperties) {
            @Override
            public void customize(NettyReactiveWebServerFactory factory) {
                factory.addServerCustomizers(httpServer -> httpServer.protocol(HttpProtocol.HTTP11, HttpProtocol.H2C));
                super.customize(factory);
            }
        };
    }

}
