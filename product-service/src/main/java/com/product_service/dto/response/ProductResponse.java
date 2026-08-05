package com.product_service.dto.response;

import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private String sku;

    private String imageUrl;

    private ProductStatus status;

    private ProductCategory category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
