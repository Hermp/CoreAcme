package org.ph.domain.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ph.application.service.impl.ProductServiceImpl;
import org.ph.domain.model.Product;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ProductServiceImpl class.
 */
class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
    }

    @Test
    void createProduct_WithoutId_ShouldGenerateId() {
        // Arrange
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.99))
                .stockQuantity(100)
                .build();

        // Act
        Product createdProduct = productService.createProduct(product);

        // Assert
        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId(), "Product ID should be generated");
        assertEquals("Test Product", createdProduct.getName());
        assertEquals("Test Description", createdProduct.getDescription());
        assertEquals(BigDecimal.valueOf(10.99), createdProduct.getPrice());
        assertEquals(100, createdProduct.getStockQuantity());
    }

    @Test
    void createProduct_WithId_ShouldKeepId() {
        // Arrange
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                .id(id)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.99))
                .stockQuantity(100)
                .build();

        // Act
        Product createdProduct = productService.createProduct(product);

        // Assert
        assertNotNull(createdProduct);
        assertEquals(id, createdProduct.getId(), "Product ID should remain unchanged");
    }

    @Test
    void updateProduct_ShouldSetId() {
        // Arrange
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(BigDecimal.valueOf(20.99))
                .stockQuantity(200)
                .build();

        // Act
        Product updatedProduct = productService.updateProduct(id, product);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(id, updatedProduct.getId(), "Product ID should be set to the provided ID");
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(20.99), updatedProduct.getPrice());
        assertEquals(200, updatedProduct.getStockQuantity());
    }

    @Test
    void getProductById_ShouldReturnEmptyOptional() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Optional<Product> result = productService.getProductById(id);

        // Assert
        assertTrue(result.isEmpty(), "Should return empty Optional as this is a pass-through method");
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList() {
        // Act
        var result = productService.getAllProducts();

        // Assert
        assertTrue(result.isEmpty(), "Should return empty list as this is a pass-through method");
    }

    @Test
    void deleteProduct_ShouldReturnFalse() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        boolean result = productService.deleteProduct(id);

        // Assert
        assertFalse(result, "Should return false as this is a pass-through method");
    }
}