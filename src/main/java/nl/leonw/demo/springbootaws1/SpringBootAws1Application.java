package nl.leonw.demo.springbootaws1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class SpringBootAws1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAws1Application.class, args);
	}

}
