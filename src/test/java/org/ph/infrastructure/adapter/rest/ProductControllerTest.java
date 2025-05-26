package org.ph.infrastructure.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ph.application.port.in.ProductUseCase;
import org.ph.domain.model.Product;
import org.ph.infrastructure.adapter.rest.dto.ProductRequest;
import org.ph.infrastructure.adapter.rest.dto.ProductResponse;
import org.ph.infrastructure.adapter.rest.mapper.ProductMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the ProductController class.
 */
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductUseCase productUseCase;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;
    private UUID testId;
    private Product testProduct;
    private ProductRequest testProductRequest;
    private ProductResponse testProductResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
        testId = UUID.randomUUID();
        
        testProduct = Product.builder()
                .id(testId)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.99))
                .stockQuantity(100)
                .build();
        
        testProductRequest = ProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.99))
                .stockQuantity(100)
                .build();
        
        testProductResponse = ProductResponse.builder()
                .id(testId)
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(10.99))
                .stockQuantity(100)
                .build();
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        when(productMapper.toDomain(any(ProductRequest.class))).thenReturn(testProduct);
        when(productUseCase.createProduct(any(Product.class))).thenReturn(testProduct);
        when(productMapper.toResponse(any(Product.class))).thenReturn(testProductResponse);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.price").value(10.99))
                .andExpect(jsonPath("$.stockQuantity").value(100));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        // Arrange
        when(productUseCase.getProductById(testId)).thenReturn(Optional.of(testProduct));
        when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(productUseCase.getProductById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", testId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Arrange
        when(productUseCase.getAllProducts()).thenReturn(List.of(testProduct));
        when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        when(productMapper.toDomain(any(ProductRequest.class))).thenReturn(testProduct);
        when(productUseCase.updateProduct(eq(testId), any(Product.class))).thenReturn(testProduct);
        when(productMapper.toResponse(testProduct)).thenReturn(testProductResponse);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(productMapper.toDomain(any(ProductRequest.class))).thenReturn(testProduct);
        when(productUseCase.updateProduct(eq(testId), any(Product.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(productUseCase.deleteProduct(testId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", testId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(productUseCase.deleteProduct(testId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", testId))
                .andExpect(status().isNotFound());
    }
}