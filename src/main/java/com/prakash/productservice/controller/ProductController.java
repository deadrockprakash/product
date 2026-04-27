package com.prakash.productservice.controller;


import com.prakash.productservice.dto.ProductDto;
import com.prakash.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@Tag(name = "Product COntroller", description = "API for managing products")
public class ProductController {
    private final ProductService  productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
        @PostMapping("/add")
        @Operation(
                summary = "Add a new product",
                description = "Creates a new product with the provided details and returns the unique ID of the created product",
                 requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                         description = "Product details to be added",
                         required = true,
                         content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                 ))
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Product created successfully",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)

                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid product details provided",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                )
        })

        public ResponseEntity<Long> saveProduct(@RequestBody ProductDto  productDto) {
            return new ResponseEntity<>(productService.saveProduct(productDto), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get product by ID", description = "Retrieves detailed information of a product using its unique ID")
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Product found successfully",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                @ApiResponse(
                        responseCode = "404",
                        description = "Product not found with the given ID",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
        })
        public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
            return new ResponseEntity<>(productDto,HttpStatus.OK);
        }
}
