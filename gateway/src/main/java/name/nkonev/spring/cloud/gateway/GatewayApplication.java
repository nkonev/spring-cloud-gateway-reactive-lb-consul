package name.nkonev.spring.cloud.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.tools.agent.ReactorDebugAgent;

// https://netty.io/wiki/reference-counted-objects.html
// while true; do curl -v -X POST -d 'foo=bar' -w "\n"  http://127.0.0.1:8282/foo; done
// curl -v -X POST -d 'foo=bar' -w "\n"  http://127.0.0.1:8282/foo
@SpringBootApplication
@RestController
public class GatewayApplication {

    public static void main(String[] args) {
        ReactorDebugAgent.init();
        SpringApplication.run(GatewayApplication.class, args);
    }

    /*@Autowired
    private WebClient webClient;

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder().build();
    }

    public static class RespDto {
        private String msg;

        public RespDto(String msg) {
            this.msg = msg;
        }

        public RespDto() {
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    @PostMapping("/manual")
    public Mono<RespDto> getManually() {
        return webClient
                .post()
                .uri("http://127.0.0.1:10001/foo")
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(RespDto.class));
    }*/
}