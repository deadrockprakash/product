package com.prakash.product_service.service;

import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.entity.Product;
import com.prakash.product_service.exception.ProductCustomException;
import com.prakash.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long saveProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .build();

       Product savedProduct =  productRepository.save(product);
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
}
