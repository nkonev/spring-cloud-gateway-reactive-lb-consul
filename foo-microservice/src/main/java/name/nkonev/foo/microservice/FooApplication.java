package name.nkonev.foo.microservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class FooApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger("foo");

	public static void main(String[] args) {
		SpringApplication.run(FooApplication.class, args);
	}

	@PostMapping("/foo")
	public String foo(@RequestBody Req req) {
		return "hi" + " " + req.getInput();
	}

}

class Req {
    private String input;

    public Req() {
    }

    public Req(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
