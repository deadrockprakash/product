package com.prakash.product_service.service;

import com.prakash.product_service.dto.PagedResponse;
import com.prakash.product_service.dto.ProductDto;

public interface ProductService {
    Long saveProduct(ProductDto productDto);

    ProductDto getProductById(Long id);

    PagedResponse<ProductDto> searchProducts(String keyword, int page, int size);
}
