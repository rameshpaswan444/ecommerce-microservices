package com.inventory_service.serviceImpl;

import com.inventory_service.dto.request.InventoryRequest;
import com.inventory_service.dto.response.InventoryResponse;
import com.inventory_service.entity.Inventory;
import com.inventory_service.exception.ResourceNotFoundException;
import com.inventory_service.mapper.InventoryMapper;
import com.inventory_service.repository.InventoryRepository;
import com.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;

    @Override
    public InventoryResponse createInventory(InventoryRequest request) {

        if (repository.existsByProductId(request.getProductId())) {
            throw new IllegalArgumentException(
                    "Inventory already exists for this product");
        }

        if (repository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException(
                    "SKU already exists");
        }

        Inventory inventory = mapper.toEntity(request);

        return mapper.toResponse(repository.save(inventory));
    }

    @Override
    public InventoryResponse getInventoryById(Long id) {

        Inventory inventory = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        return mapper.toResponse(inventory);
    }

    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {

        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        return mapper.toResponse(inventory);
    }

    @Override
    public InventoryResponse getInventoryBySku(String sku) {

        Inventory inventory = repository.findBySku(sku)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        return mapper.toResponse(inventory);
    }

    @Override
    public Page<InventoryResponse> getAllInventories(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public InventoryResponse updateInventory(
            Long id,
            InventoryRequest request) {

        Inventory inventory = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        if (!inventory.getSku().equals(request.getSku())
                && repository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setWarehouseLocation(request.getWarehouseLocation());
        inventory.setStatus(request.getStatus());

        inventory.setSku(request.getSku());

        return mapper.toResponse(repository.save(inventory));
    }

    @Override
    public void deleteInventory(Long id) {

        Inventory inventory = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Inventory not found"));

        repository.delete(inventory);
    }
}
