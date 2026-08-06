package com.inventory_service.service;

import com.inventory_service.dto.request.InventoryRequest;
import com.inventory_service.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest request);

    InventoryResponse getInventoryById(Long id);

    InventoryResponse getInventoryByProductId(Long productId);

    InventoryResponse getInventoryBySku(String sku);

    Page<InventoryResponse> getAllInventories(
            int page,
            int size,
            String sortBy,
            String sortDir);

    InventoryResponse updateInventory(Long id, InventoryRequest request);

    void deleteInventory(Long id);
}
