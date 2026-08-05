package com.product_service.mapper;

import com.product_service.dto.request.CreateProductRequest;
import com.product_service.dto.request.UpdateProductRequest;
import com.product_service.dto.response.ProductResponse;
import com.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(CreateProductRequest request);

    ProductResponse toResponse(Product product);

    void updateProductFromRequest(UpdateProductRequest request,
                                  @MappingTarget Product product);
}
