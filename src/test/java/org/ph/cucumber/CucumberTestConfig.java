package org.ph.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Configuration class for Cucumber tests.
 * This class sets up the Spring context for Cucumber tests.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberTestConfig {
    // This class is intentionally empty.
    // Its purpose is to enable Spring integration with Cucumber.
}