package org.ph.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ph.application.port.out.ProductRepository;
import org.ph.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProductUseCaseImpl class.
 */
@ExtendWith(MockitoExtension.class)
class ProductUseCaseImplTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductUseCaseImpl productUseCase;

    private Product testProduct;
    private UUID testId;

    @BeforeEach
    void setUp() {
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
    void createProduct_ShouldApplyDomainLogicAndPersist() {
        // Arrange
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productUseCase.createProduct(testProduct);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals("Test Product", result.getName());
        
        // Verify interactions
        verify(productService).createProduct(testProduct);
        verify(productRepository).save(testProduct);
    }

    @Test
    void getProductById_ShouldDelegateToRepository() {
        // Arrange
        when(productRepository.findById(testId)).thenReturn(Optional.of(testProduct));

        // Act
        Optional<Product> result = productUseCase.getProductById(testId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testProduct, result.get());
        
        // Verify interactions
        verify(productRepository).findById(testId);
        verifyNoInteractions(productService);
    }

    @Test
    void getAllProducts_ShouldDelegateToRepository() {
        // Arrange
        List<Product> products = List.of(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productUseCase.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct, result.get(0));
        
        // Verify interactions
        verify(productRepository).findAll();
        verifyNoInteractions(productService);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldApplyDomainLogicAndPersist() {
        // Arrange
        when(productRepository.existsById(testId)).thenReturn(true);
        when(productService.updateProduct(eq(testId), any(Product.class))).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productUseCase.updateProduct(testId, testProduct);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        
        // Verify interactions
        verify(productRepository).existsById(testId);
        verify(productService).updateProduct(eq(testId), any(Product.class));
        verify(productRepository).save(testProduct);
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(productRepository.existsById(testId)).thenReturn(false);

        // Act
        Product result = productUseCase.updateProduct(testId, testProduct);

        // Assert
        assertNull(result);
        
        // Verify interactions
        verify(productRepository).existsById(testId);
        verifyNoInteractions(productService);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_ShouldDelegateToRepository() {
        // Arrange
        when(productRepository.deleteById(testId)).thenReturn(true);

        // Act
        boolean result = productUseCase.deleteProduct(testId);

        // Assert
        assertTrue(result);
        
        // Verify interactions
        verify(productRepository).deleteById(testId);
        verifyNoInteractions(productService);
    }
}