package com.inventory_service.mapper;

import com.inventory_service.dto.request.InventoryRequest;
import com.inventory_service.dto.response.InventoryResponse;
import com.inventory_service.entity.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    Inventory toEntity(InventoryRequest request);

    InventoryResponse toResponse(Inventory inventory);
}
