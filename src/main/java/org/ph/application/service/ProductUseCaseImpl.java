package org.ph.application.service;

import lombok.RequiredArgsConstructor;
import org.ph.application.port.in.ProductUseCase;
import org.ph.application.port.out.ProductRepository;
import org.ph.domain.model.Product;
import org.ph.domain.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the ProductUseCase interface.
 * This class coordinates between the domain service and the repository.
 */
@Service
@RequiredArgsConstructor
public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductService productService;
    private final ProductRepository productRepository;

    /**
     * Creates a new product.
     *
     * @param product The product to create
     * @return The created product with generated ID
     */
    @Override
    public Product createProduct(Product product) {
        // Apply domain logic
        Product processedProduct = productService.createProduct(product);
        
        // Persist the product
        return productRepository.save(processedProduct);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return An Optional containing the product if found, or empty if not found
     */
    @Override
    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    /**
     * Retrieves all products.
     *
     * @return A list of all products
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Updates an existing product.
     *
     * @param id The ID of the product to update
     * @param product The updated product data
     * @return The updated product, or null if the product was not found
     */
    @Override
    public Product updateProduct(UUID id, Product product) {
        if (!productRepository.existsById(id)) {
            return null;
        }
        
        // Apply domain logic
        Product processedProduct = productService.updateProduct(id, product);
        
        // Persist the product
        return productRepository.save(processedProduct);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return true if the product was deleted, false if it was not found
     */
    @Override
    public boolean deleteProduct(UUID id) {
        return productRepository.deleteById(id);
    }
}