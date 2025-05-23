package org.ph.application.port.out;

import org.ph.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port interface for product persistence operations.
 * This is part of the application layer and defines the contract that
 * infrastructure adapters must implement to provide persistence capabilities.
 */
public interface ProductRepository {
    /**
     * Saves a product to the repository.
     *
     * @param product The product to save
     * @return The saved product with any generated IDs or updated fields
     */
    Product save(Product product);

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to find
     * @return An Optional containing the product if found, or empty if not found
     */
    Optional<Product> findById(UUID id);

    /**
     * Finds all products in the repository.
     *
     * @return A list of all products
     */
    List<Product> findAll();

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return true if the product was deleted, false if it was not found
     */
    boolean deleteById(UUID id);

    /**
     * Checks if a product with the given ID exists.
     *
     * @param id The ID to check
     * @return true if a product with the ID exists, false otherwise
     */
    boolean existsById(UUID id);
}