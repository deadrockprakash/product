package com.prakash.product_service.controller;


import com.prakash.product_service.controller.api.ProductApi;
import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController implements ProductApi {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADD')")
    public ResponseEntity<Long> saveProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.saveProduct(productDto), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VIEW','VIEW_ALL')")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
}
