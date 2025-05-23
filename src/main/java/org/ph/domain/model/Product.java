package org.ph.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Product entity in the domain layer.
 * This is a core business entity that represents a product in our system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
}