package com.product_service.controller;

import com.product_service.dto.request.CreateProductRequest;
import com.product_service.dto.request.UpdateProductRequest;
import com.product_service.dto.response.PageResponse;
import com.product_service.dto.response.ProductResponse;
import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;
import com.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {

        ProductResponse response = service.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction
    ) {

        return ResponseEntity.ok(
                service.getAllProducts(page, size, sortBy, direction)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id){

        return ResponseEntity.ok(service.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {

        return ResponseEntity.ok(service.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        service.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ProductResponse>> searchProducts(
//
//            @RequestParam String keyword) {
//
//        return ResponseEntity.ok(
//                service.searchByName(keyword)
//        );
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<ProductResponse>> getByCategory(
//
//            @PathVariable ProductCategory category) {
//
//        return ResponseEntity.ok(
//                service.getByCategory(category)
//        );
//    }
//
//    @GetMapping("/status/{status}")
//    public ResponseEntity<List<ProductResponse>> getByStatus(
//
//            @PathVariable ProductStatus status) {
//
//        return ResponseEntity.ok(
//                service.getByStatus(status)
//        );
//    }
//
//    @GetMapping("/price")
//    public ResponseEntity<List<ProductResponse>> getByPrice(
//
//            @RequestParam BigDecimal min,
//
//            @RequestParam BigDecimal max) {
//
//        return ResponseEntity.ok(
//                service.getByPriceRange(min, max)
//        );
//    }

    @GetMapping("/filter")
    public ResponseEntity<PageResponse<ProductResponse>> filterProducts(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) ProductCategory category,

            @RequestParam(required = false) ProductStatus status,

            @RequestParam(required = false) BigDecimal minPrice,

            @RequestParam(required = false) BigDecimal maxPrice,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction
    ) {

        return ResponseEntity.ok(
                service.filterProducts(
                        keyword,
                        category,
                        status,
                        minPrice,
                        maxPrice,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }
}
