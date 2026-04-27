package com.prakash.productservice.controller;


import com.prakash.productservice.dto.ProductDto;
import com.prakash.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        @Tag(name = "ProductController", description = "API for managing products")
        public ResponseEntity<Long> saveProduct(@RequestBody ProductDto  productDto) {
            return new ResponseEntity<>(productService.saveProduct(productDto), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get product by ID", description = "Retrieves detailed information of a product using its unique ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Product found successfully"),
                @ApiResponse(responseCode = "404", description = "Product not found with the given ID")
        })
        public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
            return new ResponseEntity<>(productDto,HttpStatus.OK);
        }
}
