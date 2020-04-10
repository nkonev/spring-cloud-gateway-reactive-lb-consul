package name.nkonev.foo.microservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class FooApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger("foo");

	public static void main(String[] args) {
		SpringApplication.run(FooApplication.class, args);
	}

	@RequestMapping("/")
	public String foo() {
		LOGGER.info("Invoked /foo");
		return "hi";
	}

}
