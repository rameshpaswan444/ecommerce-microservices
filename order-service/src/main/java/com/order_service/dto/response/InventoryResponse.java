package com.order_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private Long id;

    private Long productId;

    private String sku;

    private Integer availableQuantity;

    private Integer reservedQuantity;

    private String warehouseLocation;

    private String status;
}