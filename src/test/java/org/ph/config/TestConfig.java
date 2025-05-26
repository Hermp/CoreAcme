package org.ph.config;

import org.ph.application.port.in.ProductUseCase;
import org.ph.application.port.out.ProductRepository;
import org.ph.application.service.ProductUseCaseImpl;
import org.ph.application.service.ProductService;
import org.ph.application.service.impl.ProductServiceImpl;
import org.ph.infrastructure.adapter.repository.InMemoryProductRepository;
import org.ph.infrastructure.adapter.rest.ProductController;
import org.ph.infrastructure.adapter.rest.mapper.ProductMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Test configuration for integration tests.
 * This class provides the necessary beans for the Spring context in tests.
 */
@Configuration
@EnableAutoConfiguration
public class TestConfig {

    /**
     * Creates a ProductService bean for testing.
     *
     * @return A ProductService instance
     */
    @Bean
    public ProductService productService() {
        return new ProductServiceImpl();
    }

    /**
     * Creates a ProductRepository bean for testing.
     *
     * @return A ProductRepository instance
     */
    @Bean
    public ProductRepository productRepository() {
        return new InMemoryProductRepository();
    }

    /**
     * Creates a ProductUseCase bean for testing.
     *
     * @param productService The ProductService bean
     * @param productRepository The ProductRepository bean
     * @return A ProductUseCase instance
     */
    @Bean
    public ProductUseCase productUseCase(ProductService productService, ProductRepository productRepository) {
        return new ProductUseCaseImpl(productService, productRepository);
    }

    /**
     * Creates a ProductMapper bean for testing.
     *
     * @return A ProductMapper instance
     */
    @Bean
    public ProductMapper productMapper() {
        return new ProductMapper();
    }

    /**
     * Creates a ProductController bean for testing.
     *
     * @param productUseCase The ProductUseCase bean
     * @param productMapper The ProductMapper bean
     * @return A ProductController instance
     */
    @Bean
    public ProductController productController(ProductUseCase productUseCase, ProductMapper productMapper) {
        return new ProductController(productUseCase, productMapper);
    }
}
