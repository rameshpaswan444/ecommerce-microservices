package com.product_service.service;

import com.product_service.dto.request.CreateProductRequest;
import com.product_service.dto.request.UpdateProductRequest;
import com.product_service.dto.response.PageResponse;
import com.product_service.dto.response.ProductResponse;
import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    PageResponse<ProductResponse> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction
    );

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, UpdateProductRequest request);

    void deleteProduct(Long id);

//    List<ProductResponse> searchByName(String keyword);
//
//    List<ProductResponse> getByCategory(ProductCategory category);
//
//    List<ProductResponse> getByStatus(ProductStatus status);
//
//    List<ProductResponse> getByPriceRange(
//            BigDecimal min,
//            BigDecimal max
//    );

    PageResponse<ProductResponse> filterProducts(
            String keyword,
            ProductCategory category,
            ProductStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String direction
    );
}
