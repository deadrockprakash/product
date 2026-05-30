package com.prakash.product_service.service;

import com.prakash.product_service.dto.PagedResponse;
import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.entity.Product;
import com.prakash.product_service.event.ProductCreatedEvent;
import com.prakash.product_service.exception.ProductCustomException;
import com.prakash.product_service.messaging.ProductEventPublisher;
import com.prakash.product_service.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        Product product = findProductOrThrow(id);
        return toDto(product);
    }

    @Override
    public PagedResponse<ProductDto> searchProducts(String keyword, int page, int size) {
        int pageNumber = Math.max(page, 0);
        int pageSize = Math.clamp(size, 1, 100);
        Pageable sortedPageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String searchKeyword = keyword == null ? "" : keyword.trim();
        Page<Product> products = searchKeyword.isBlank()
                ? productRepository.findAll(sortedPageable)
                : productRepository.searchByKeyword(searchKeyword, pageable);

        return toPagedResponse(products);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = findProductOrThrow(id);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        return toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductCustomException("Product not found " + id, "PRODUCT_NOT_FOUND"));
    }

    private ProductDto toDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .build();
    }

    private PagedResponse<ProductDto> toPagedResponse(Page<Product> products) {
        Page<ProductDto> productDtos = products.map(this::toDto);
        return PagedResponse.<ProductDto>builder()
                .content(productDtos.getContent())
                .page(productDtos.getNumber())
                .size(productDtos.getSize())
                .totalElements(productDtos.getTotalElements())
                .totalPages(productDtos.getTotalPages())
                .last(productDtos.isLast())
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
