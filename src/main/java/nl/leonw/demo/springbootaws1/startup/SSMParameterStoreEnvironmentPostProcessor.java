package nl.leonw.demo.springbootaws1.startup;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.Profiles;

import java.util.stream.Collectors;

/**
 * Retrieve configuration from the Systems Manager Parameter Store.
 * <p>
 * reformats parameter names to 1) dump the path and 2) conform to Spring convention
 */
public class SSMParameterStoreEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if ( environment.acceptsProfiles(Profiles.of(GithubEnvironmentPostProcessor.PROFILE_NAME)) ) {
            System.out.println("Git profile active. Aborting the SSMParameterStoreEnvironmentPostProcessor");
            return;
        }


        // The logging subsystem has not yet initialized so just write to stdout for now
        System.out.println("Retrieving configuration from the SSM Parameter Store");

        // According to the documentation https://docs.spring.io/spring-boot/docs/2.1.11.RELEASE/reference/html/howto-spring-boot-application.html#howto-customize-the-environment-or-application-context
        // The normal property sources have already been loaded. This PostProcessor just applies overrides


        var paramPath = environment.getRequiredProperty("config.aws.ssm.path");
        var paramRequest = new GetParametersByPathRequest()
                .withPath(paramPath)
                .withWithDecryption(true)
                .withRecursive(true);

        var region = environment.getRequiredProperty("config.aws.ssm.region");
        var ssm = AWSSimpleSystemsManagementClientBuilder.standard().withRegion(region).build();

        var response = ssm.getParametersByPath(paramRequest);

        if (response.getNextToken() != null) {
            System.out.println("There is a NEXT token. So not all data is retrieved");
            // TODO Make this recursive. Even if I expect it can always return the one or 2 props I use here
            throw new IllegalStateException("Could not load all properties");
        }

        // Funny stuff on the collect because a MapPropertySource wants a Map<String, Object>, not Map<String, String>
        var reformatter = new SSMParameterReformatter();
        var params = response.getParameters()
                .stream()
                .filter(p -> p.getValue() != null)
                .collect(Collectors.<Parameter, String, Object>toMap(
                        p -> reformatter.reformat(p.getName()),
                        Parameter::getValue)
                );

        params.keySet().forEach(k -> System.out.println("Read parameter '" + k + "' from keystore"));
        // First and Last are highest and lowest precedence
        environment.getPropertySources()
                .addFirst(new MapPropertySource("SSMParamStore", params));

        System.out.println(params.size() + " parameters have been added to the context");
    }
}
