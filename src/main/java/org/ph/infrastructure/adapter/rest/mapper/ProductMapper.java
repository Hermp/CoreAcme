package org.ph.infrastructure.adapter.rest.mapper;

import org.ph.domain.model.Product;
import org.ph.infrastructure.adapter.rest.dto.ProductRequest;
import org.ph.infrastructure.adapter.rest.dto.ProductResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper class to convert between DTOs and domain entities.
 * This helps maintain the separation between the infrastructure and domain layers.
 */
@Component
public class ProductMapper {

    /**
     * Converts a ProductRequest DTO to a Product domain entity.
     *
     * @param request The ProductRequest DTO
     * @return A Product domain entity
     */
    public Product toDomain(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();
    }

    /**
     * Converts a Product domain entity to a ProductResponse DTO.
     *
     * @param product The Product domain entity
     * @return A ProductResponse DTO
     */
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}