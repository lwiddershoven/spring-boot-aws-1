package nl.leonw.demo.springbootaws1;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringBootAws1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAws1Application.class, args);
	}

}

@Slf4j
@Component
class Something {

	@Value("${test-string}")
	private String testString;

	@PostConstruct
	public void print() {
		log.info("Value of test-string: " + testString);
	}
}
