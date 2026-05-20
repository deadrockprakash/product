package com.prakash.product_service.service;

import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.entity.Product;
import com.prakash.product_service.event.ProductCreatedEvent;
import com.prakash.product_service.exception.ProductCustomException;
import com.prakash.product_service.messaging.ProductEventPublisher;
import com.prakash.product_service.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductEventPublisher productEventPublisher;

    public ProductServiceImpl(ProductRepository productRepository, ProductEventPublisher productEventPublisher) {
        this.productRepository = productRepository;
        this.productEventPublisher = productEventPublisher;
    }

    public Long saveProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .build();

       Product savedProduct =  productRepository.save(product);
       log.info("Product saved with id: {}", savedProduct.getId());
        productEventPublisher.publishProductCreated(toProductCreatedEvent(savedProduct));
        log.info("Product created event published");
        return savedProduct.getId();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductCustomException("Product not found " + id, "Product_not_found"));
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .build();
    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        String searchKeyword = keyword == null ? "" : keyword.trim();
        List<Product> products = searchKeyword.isBlank()
                ? productRepository.findAll()
                : productRepository.searchByKeyword(searchKeyword);

        return products.stream()
                .map(this::toDto)
                .toList();
    }

    private ProductDto toDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .build();
    }

    private ProductCreatedEvent toProductCreatedEvent(Product product) {
        log.info("Creating product created event for product: {}", product);
        return new ProductCreatedEvent(
                UUID.randomUUID().toString(),
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                Instant.now()
        );
    }
}
