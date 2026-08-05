package com.product_service.specification;

import com.product_service.entity.Product;
import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank()
                        ? null
                        : cb.like(
                        cb.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%"
                );
    }

    public static Specification<Product> hasCategory(ProductCategory category) {
        return (root, query, cb) ->
                category == null
                        ? null
                        : cb.equal(root.get("category"), category);
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

}
