package com.prakash.product_service.service;

import com.prakash.product_service.dto.ProductDto;

import java.util.List;

public interface ProductService {
    Long saveProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    List<ProductDto> searchProducts(String keyword);
}
