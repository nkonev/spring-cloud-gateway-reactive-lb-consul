package name.nkonev.foo.microservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@SpringBootApplication
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger("foo");

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/v1/test-route")
	public String foo(@RequestParam(name = "sleep", required = false, defaultValue = "0") int secondsToSleep) throws Exception {
		LOGGER.info("Invoked with {}", secondsToSleep);
		if (secondsToSleep > 0) {
			TimeUnit.SECONDS.sleep(secondsToSleep);
		}
		return "hi";
	}

}
