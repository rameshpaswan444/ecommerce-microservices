package com.inventory_service.entity;

import com.inventory_service.common.BaseEntity;

import com.inventory_service.enums.InventoryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends BaseEntity {

    @Column(nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Column(nullable = false)
    @Builder.Default
    private Integer reservedQuantity = 0;

    private String warehouseLocation;

    @Enumerated(EnumType.STRING)
    private InventoryStatus status;
}
