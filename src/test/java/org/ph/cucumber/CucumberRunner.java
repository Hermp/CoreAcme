package org.ph.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.ph.ApplicationContextTest;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Runner class for Cucumber tests.
 * This class configures Cucumber to use the feature files and step definitions.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"org.ph.cucumber"},
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CucumberRunner {
    // This class is intentionally empty.
    // Its purpose is to run Cucumber tests.
}