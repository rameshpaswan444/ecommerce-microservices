package com.product_service.repository;

import com.product_service.entity.Product;
import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
}
