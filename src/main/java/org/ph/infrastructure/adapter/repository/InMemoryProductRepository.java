package org.ph.infrastructure.adapter.repository;

import org.ph.application.port.out.ProductRepository;
import org.ph.domain.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the ProductRepository interface.
 * This is part of the infrastructure layer and provides a simple
 * in-memory storage solution for products.
 */
@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> products = new ConcurrentHashMap<>();

    /**
     * Saves a product to the repository.
     *
     * @param product The product to save
     * @return The saved product
     */
    @Override
    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to find
     * @return An Optional containing the product if found, or empty if not found
     */
    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    /**
     * Finds all products in the repository.
     *
     * @return A list of all products
     */
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete
     * @return true if the product was deleted, false if it was not found
     */
    @Override
    public boolean deleteById(UUID id) {
        return products.remove(id) != null;
    }

    /**
     * Checks if a product with the given ID exists.
     *
     * @param id The ID to check
     * @return true if a product with the ID exists, false otherwise
     */
    @Override
    public boolean existsById(UUID id) {
        return products.containsKey(id);
    }
}