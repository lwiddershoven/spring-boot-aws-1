package nl.leonw.demo.springbootaws1.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Retrieve configuration from the Systems Manager Parameter Store.
 */
public class SSMParameterStoreEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // The logging subsystem has not yet initialized so just write to stdout for now
        System.out.println("Retrieving configuration from the SSM Parameter Store");

        // //		AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

    }
}
