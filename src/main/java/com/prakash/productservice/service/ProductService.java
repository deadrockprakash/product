package com.prakash.productservice.service;

import com.prakash.productservice.dto.ProductDto;

public interface ProductService {
    Long saveProduct(ProductDto productDto);

    ProductDto getProductById(Long id);
}
