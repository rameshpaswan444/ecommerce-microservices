package com.product_service.serviceImpl;

import com.product_service.dto.request.CreateProductRequest;
import com.product_service.dto.request.UpdateProductRequest;
import com.product_service.dto.response.PageResponse;
import com.product_service.dto.response.ProductResponse;
import com.product_service.entity.Product;
import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;
import com.product_service.exception.DuplicateResourceException;
import com.product_service.exception.ResourceNotFoundException;
import com.product_service.mapper.ProductMapper;
import com.product_service.repository.ProductRepository;
import com.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.product_service.specification.ProductSpecification.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {

        if (repository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("SKU already exists");
        }

        Product product = mapper.toEntity(request);

        product.setStatus(ProductStatus.ACTIVE);

        Product savedProduct = repository.save(product);

        return mapper.toResponse(savedProduct);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = repository.findAll(pageable);

        List<ProductResponse> responses = productPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return PageResponse.<ProductResponse>builder()
                .content(responses)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + id));

        return mapper.toResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id,
                                         UpdateProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + id));

        if (!product.getSku().equals(request.getSku())
                && repository.existsBySku(request.getSku())) {

            throw new DuplicateResourceException("SKU already exists");
        }

        mapper.updateProductFromRequest(request, product);

        Product updatedProduct = repository.save(product);

        return mapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + id));

        repository.delete(product);
    }

//    @Override
//    public List<ProductResponse> searchByName(String keyword) {
//
//        return repository.findByNameContainingIgnoreCase(keyword)
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    public List<ProductResponse> getByCategory(ProductCategory category) {
//
//        return repository.findByCategory(category)
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    public List<ProductResponse> getByStatus(ProductStatus status) {
//
//        return repository.findByStatus(status)
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    public List<ProductResponse> getByPriceRange(
//            BigDecimal min,
//            BigDecimal max) {
//
//        return repository.findByPriceBetween(min, max)
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }

    @Override
    public PageResponse<ProductResponse> filterProducts(
            String keyword,
            ProductCategory category,
            ProductStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String direction) {

        Specification<Product> specification = Specification.allOf(
                hasKeyword(keyword),
                hasCategory(category),
                hasStatus(status),
                minPrice(minPrice),
                maxPrice(maxPrice)
        );

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage =
                repository.findAll(specification, pageable);

        List<ProductResponse> responses = productPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return PageResponse.<ProductResponse>builder()
                .content(responses)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }
}
