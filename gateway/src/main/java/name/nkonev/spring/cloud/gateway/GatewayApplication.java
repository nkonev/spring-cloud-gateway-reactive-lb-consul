package name.nkonev.spring.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

// https://netty.io/wiki/reference-counted-objects.html
// while true; do curl -v -X POST -d 'foo=bar' -w "\n"  http://127.0.0.1:8282/foo; done
// curl -v -X POST -d 'foo=bar' -w "\n"  http://127.0.0.1:8282/foo
@SpringBootApplication
@RestController
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}