package org.ph;

import org.junit.jupiter.api.Test;
import org.ph.application.port.in.ProductUseCase;
import org.ph.application.port.out.ProductRepository;
import org.ph.application.service.impl.ProductServiceImpl;
import org.ph.config.TestConfig;
import org.ph.application.service.ProductService;
import org.ph.infrastructure.adapter.rest.ProductController;
import org.ph.infrastructure.adapter.rest.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test to verify that the Spring application context loads correctly.
 * This ensures that all beans are properly configured and can be autowired.
 */
@SpringBootTest(classes = TestConfig.class)
public class ApplicationContextTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // This test will fail if the application context cannot be loaded
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void allBeansAreLoaded() {
        // Verify that all required beans are available in the application context
        assertNotNull(applicationContext.getBean(ProductServiceImpl.class), "ProductService bean should be available");
        assertNotNull(applicationContext.getBean(ProductUseCase.class), "ProductUseCase bean should be available");
        assertNotNull(applicationContext.getBean(ProductRepository.class), "ProductRepository bean should be available");
        assertNotNull(applicationContext.getBean(ProductController.class), "ProductController bean should be available");
        assertNotNull(applicationContext.getBean(ProductMapper.class), "ProductMapper bean should be available");
    }
}
