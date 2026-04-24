package com.prakash.productservice.controller;


import com.prakash.productservice.dto.ProductDto;
import com.prakash.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")

public class ProductController {
    private final ProductService  productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
        @PostMapping("/add")
        public ResponseEntity<Long> saveProduct(@RequestBody ProductDto  productDto) {
            return new ResponseEntity<>(productService.saveProduct(productDto), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
            return new ResponseEntity<>(productDto,HttpStatus.OK);
        }
}
