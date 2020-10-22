package nl.leonw.demo.springbootaws1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nl.leonw.demo.springbootaws1.startup.SSMParameterStoreEnvironmentPostProcessor.PROFILE_SKIP_AWS_CONNECTIONS;

@SpringBootTest()
@ActiveProfiles(PROFILE_SKIP_AWS_CONNECTIONS)
class SpringBootAws1ApplicationTests {

    @Test
    void contextLoads() {
    }

}
