package name.nkonev.foo.microservice;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpProtocol;

public class HttpServer {
    // curl 'http://localhost:8089'
    public static void main(String[] args) {
        DisposableServer server =
                reactor.netty.http.server.HttpServer.create()
                        .port(8089)
                        .protocol(HttpProtocol.HTTP11)
                        .handle((request, response) -> response.sendString(Mono.just("hello from http")))
                        .bindNow();

        server.onDispose()
                .block();

    }
}
