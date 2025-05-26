package org.ph.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.ph.config.TestConfig;
import org.ph.domain.model.Product;
import org.ph.infrastructure.adapter.rest.dto.ProductRequest;
import org.ph.infrastructure.adapter.rest.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the Product API.
 * These tests verify that all components work together correctly.
 */
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullProductLifecycle() throws Exception {
        // Create a product
        ProductRequest createRequest = ProductRequest.builder()
                .name("Integration Test Product")
                .description("Product for integration testing")
                .price(BigDecimal.valueOf(29.99))
                .stockQuantity(50)
                .build();

        MvcResult createResult = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Test Product"))
                .andExpect(jsonPath("$.description").value("Product for integration testing"))
                .andExpect(jsonPath("$.price").value(29.99))
                .andExpect(jsonPath("$.stockQuantity").value(50))
                .andReturn();

        // Extract the created product ID
        ProductResponse createdProduct = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ProductResponse.class
        );
        UUID productId = createdProduct.getId();
        assertNotNull(productId, "Product ID should not be null");

        // Get the product by ID
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Integration Test Product"));

        // Get all products and verify our product is in the list
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == '" + productId + "')]").exists());

        // Update the product
        ProductRequest updateRequest = ProductRequest.builder()
                .name("Updated Integration Test Product")
                .description("Updated product for integration testing")
                .price(BigDecimal.valueOf(39.99))
                .stockQuantity(100)
                .build();

        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Integration Test Product"))
                .andExpect(jsonPath("$.description").value("Updated product for integration testing"))
                .andExpect(jsonPath("$.price").value(39.99))
                .andExpect(jsonPath("$.stockQuantity").value(100));

        // Delete the product
        mockMvc.perform(delete("/api/products/{id}", productId))
                .andExpect(status().isNoContent());

        // Verify the product is deleted
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Create a product with invalid data (missing required fields)
        ProductRequest invalidRequest = ProductRequest.builder()
                .description("Invalid product")
                .stockQuantity(10)
                .build();

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProductById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/api/products/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        ProductRequest updateRequest = ProductRequest.builder()
                .name("Non-existent Product")
                .description("This product doesn't exist")
                .price(BigDecimal.valueOf(19.99))
                .stockQuantity(30)
                .build();

        mockMvc.perform(put("/api/products/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(delete("/api/products/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}
