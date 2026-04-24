package com.prakash.productservice.service;

import com.prakash.productservice.dto.ProductDto;
import com.prakash.productservice.entity.Product;

public interface ProductService {
    Long saveProduct(ProductDto productDto);

    ProductDto getProductByid(Long id);
}
