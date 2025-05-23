package org.ph.application.port.in;

import org.ph.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Input port interface for product operations.
 * This is part of the application layer and defines the contract that
 * infrastructure adapters (like controllers) use to interact with the application.
 */
public interface ProductUseCase {
    /**
     * Creates a new product.
     *
     * @param product The product to create
     * @return The created product with generated ID
     */
    Product createProduct(Product product);

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return An Optional containing the product if found, or empty if not found
     */
    Optional<Product> getProductById(UUID id);

    /**
     * Retrieves all products.
     *
     * @return A list of all products
     */
    List<Product> getAllProducts();

    /**
     * Updates an existing product.
     *
     * @param id The ID of the product to update
     * @param product The updated product data
     * @return The updated product, or null if the product was not found
     */
    Product updateProduct(UUID id, Product product);

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return true if the product was deleted, false if it was not found
     */
    boolean deleteProduct(UUID id);
}