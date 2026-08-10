package com.order_service.client;

import com.order_service.config.FeignConfig;
import com.order_service.dto.request.ReserveInventoryRequest;
import com.order_service.dto.response.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "INVENTORY-SERVICE", configuration = FeignConfig.class)
public interface InventoryClient {

    @GetMapping("/api/inventories/product/{productId}")
    InventoryResponse getInventoryByProductId(
            @PathVariable("productId") Long productId
    );

    @PostMapping("/api/inventories/{productId}/reserve")
    InventoryResponse reserveInventory(
            @PathVariable("productId") Long productId,
            @RequestBody ReserveInventoryRequest request
    );

    @PostMapping("/api/inventories/{productId}/release")
    InventoryResponse releaseInventory(
            @PathVariable("productId") Long productId,
            @RequestBody ReserveInventoryRequest request
    );
}
