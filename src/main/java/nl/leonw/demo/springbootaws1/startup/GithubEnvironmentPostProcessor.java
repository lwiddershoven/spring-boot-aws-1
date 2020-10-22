package nl.leonw.demo.springbootaws1.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Class to set mock properties normally loaded from the parameter store, Mostly for tests.
 * Maybe we can think of something better than to inject test code in production code.
 */
public class GithubEnvironmentPostProcessor implements EnvironmentPostProcessor {
    public static final String PROFILE_NAME = "github";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("YAY");
    }
}
