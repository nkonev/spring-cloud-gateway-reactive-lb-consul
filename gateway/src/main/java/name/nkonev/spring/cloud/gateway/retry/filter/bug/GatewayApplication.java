package name.nkonev.spring.cloud.gateway.retry.filter.bug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// curl -v -X POST -d 'foo=bar' -w "\n"  http://127.0.0.1:8282/post
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}