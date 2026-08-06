package com.inventory_service.controller;

import com.inventory_service.dto.request.InventoryRequest;
import com.inventory_service.dto.response.InventoryResponse;
import com.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public InventoryResponse createInventory(
            @Valid @RequestBody InventoryRequest request) {

        return inventoryService.createInventory(request);
    }

    @GetMapping("/{id}")
    public InventoryResponse getInventoryById(
            @PathVariable Long id) {

        return inventoryService.getInventoryById(id);
    }

    @GetMapping("/product/{productId}")
    public InventoryResponse getInventoryByProductId(
            @PathVariable Long productId) {

        return inventoryService.getInventoryByProductId(productId);
    }

    @GetMapping("/sku/{sku}")
    public InventoryResponse getInventoryBySku(
            @PathVariable String sku) {

        return inventoryService.getInventoryBySku(sku);
    }

    @GetMapping
    public Page<InventoryResponse> getAllInventories(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String sortDir) {

        return inventoryService.getAllInventories(
                page,
                size,
                sortBy,
                sortDir);
    }

    @PutMapping("/{id}")
    public InventoryResponse updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request) {

        return inventoryService.updateInventory(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteInventory(
            @PathVariable Long id) {

        inventoryService.deleteInventory(id);
    }
}
