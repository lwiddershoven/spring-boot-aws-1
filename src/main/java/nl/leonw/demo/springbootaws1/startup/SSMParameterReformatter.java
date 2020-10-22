package nl.leonw.demo.springbootaws1.startup;

/**
 * Small class that reformats the SSM Parameter keys to be in line with the Spring hierarchy.
 * <p>
 * The SSM Parameter Store uses '/' as a hierarchical separator, much like a filesystem.
 * Spring properties on the other hand are separated by means of a '.'
 * <p>
 * This class uses my own convention that the structure of my hierarchy is:
 * /{environment}/{service}/{properties}
 * where the properties themselves may have a hierarchy.
 * <p>
 * First, the first 2 layers of the property name are removed ; these were just the path
 * to find *our* properties. For the rest, we replace all '/' with '.'
 * <p>
 * Examples:
 * /Dev/springbootaws1/test-string -> test-string
 * /Dev/springbootaws1/database/password -> database.password
 * /Dev/springbootaws1/database.password -> database.password
 * <p>
 * Do note that without this class constructs like @Value("${/Dev/springbootaws1/test-string}")
 * work perfectly fine. They just look strange and cumbersome.
 * <p>
 * Decisions:
 * - Do we want to remove the first 2 levels (so, consistent with our naming convention) or do we want to
 * remove config.aws.ssm.path ? Choice: follow naming conventions.
 * - Do we check array length after split? No, we assume conventions and thus there must be enough items in
 * the list. An exception if not so seems acceptable.
 */
public class SSMParameterReformatter {
    private static final String SSM_SEPARATOR = "/";
    private static final String SPRING_SEPARATOR = ".";

    public String reformat(String key) {
        var lastPart = key.split(SSM_SEPARATOR, 4)[3].trim();
        return lastPart.replace(SSM_SEPARATOR, SPRING_SEPARATOR);
    }
}
