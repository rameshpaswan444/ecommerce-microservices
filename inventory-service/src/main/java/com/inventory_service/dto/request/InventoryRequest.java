package com.inventory_service.dto.request;

import com.inventory_service.enums.InventoryStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class InventoryRequest {

        @NotNull
        private Long productId;

        @NotBlank
        private String sku;

        @NotNull
        @Min(0)
        private Integer availableQuantity;

        @Min(0)
        @Builder.Default
        private Integer reservedQuantity = 0;

        private String warehouseLocation;

        @NotNull
        private InventoryStatus status;
    }

