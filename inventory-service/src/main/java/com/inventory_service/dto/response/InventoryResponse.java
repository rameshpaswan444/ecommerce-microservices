package com.inventory_service.dto.response;

import com.inventory_service.enums.InventoryStatus;
import lombok.*;

import java.time.LocalDateTime;

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

    private InventoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
