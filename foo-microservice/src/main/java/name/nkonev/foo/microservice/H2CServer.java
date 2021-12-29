package name.nkonev.foo.microservice;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;

public class H2CServer {
    // curl --http2-prior-knowledge  'http://localhost:8088'
    public static void main(String[] args) {
        DisposableServer server =
                HttpServer.create()
                        .port(8088)
                        .protocol(HttpProtocol.H2C)
                        .handle((request, response) -> response.sendString(Mono.just("hello from h2c")))
                        .bindNow();

        server.onDispose()
                .block();

    }
}
