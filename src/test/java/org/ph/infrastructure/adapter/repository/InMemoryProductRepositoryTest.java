package org.ph.infrastructure.adapter.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ph.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the InMemoryProductRepository class.
 */
class InMemoryProductRepositoryTest {

    private InMemoryProductRepository repository;
    private Product testProduct;
    private UUID testId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProductRepository();
        testId = UUID.randomUUID();
        testProduct = Product.builder()
                .id(testId)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.99))
                .stockQuantity(100)
                .build();
    }

    @Test
    void save_ShouldStoreProduct() {
        // Act
        Product savedProduct = repository.save(testProduct);

        // Assert
        assertEquals(testProduct, savedProduct);
        assertTrue(repository.existsById(testId));
    }

    @Test
    void findById_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        repository.save(testProduct);

        // Act
        Optional<Product> result = repository.findById(testId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testProduct, result.get());
    }

    @Test
    void findById_WhenProductDoesNotExist_ShouldReturnEmptyOptional() {
        // Act
        Optional<Product> result = repository.findById(UUID.randomUUID());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_WhenRepositoryIsEmpty_ShouldReturnEmptyList() {
        // Act
        List<Product> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_WhenRepositoryHasProducts_ShouldReturnAllProducts() {
        // Arrange
        repository.save(testProduct);
        
        Product anotherProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Another Product")
                .description("Another Description")
                .price(BigDecimal.valueOf(20.99))
                .stockQuantity(200)
                .build();
        repository.save(anotherProduct);

        // Act
        List<Product> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testProduct));
        assertTrue(result.contains(anotherProduct));
    }

    @Test
    void deleteById_WhenProductExists_ShouldRemoveProductAndReturnTrue() {
        // Arrange
        repository.save(testProduct);

        // Act
        boolean result = repository.deleteById(testId);

        // Assert
        assertTrue(result);
        assertFalse(repository.existsById(testId));
    }

    @Test
    void deleteById_WhenProductDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean result = repository.deleteById(UUID.randomUUID());

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_WhenProductExists_ShouldReturnTrue() {
        // Arrange
        repository.save(testProduct);

        // Act
        boolean result = repository.existsById(testId);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_WhenProductDoesNotExist_ShouldReturnFalse() {
        // Act
        boolean result = repository.existsById(UUID.randomUUID());

        // Assert
        assertFalse(result);
    }
}