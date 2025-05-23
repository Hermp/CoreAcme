package org.ph.infrastructure.adapter.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ph.application.port.in.ProductUseCase;
import org.ph.domain.model.Product;
import org.ph.infrastructure.adapter.rest.dto.ProductRequest;
import org.ph.infrastructure.adapter.rest.dto.ProductResponse;
import org.ph.infrastructure.adapter.rest.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for product operations.
 * This controller exposes the API endpoints for CRUD operations on products.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;

    /**
     * Creates a new product.
     *
     * @param request The product data
     * @return The created product
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = productMapper.toDomain(request);
        Product createdProduct = productUseCase.createProduct(product);
        return new ResponseEntity<>(productMapper.toResponse(createdProduct), HttpStatus.CREATED);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return The product if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        return productUseCase.getProductById(id)
                .map(product -> ResponseEntity.ok(productMapper.toResponse(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all products.
     *
     * @return A list of all products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productUseCase.getAllProducts().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    /**
     * Updates an existing product.
     *
     * @param id The ID of the product to update
     * @param request The updated product data
     * @return The updated product if found, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        
        Product product = productMapper.toDomain(request);
        Product updatedProduct = productUseCase.updateProduct(id, product);
        
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(productMapper.toResponse(updatedProduct));
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return 204 No Content if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        boolean deleted = productUseCase.deleteProduct(id);
        
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.noContent().build();
    }
}