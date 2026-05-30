package com.prakash.product_service.controller.api;

import com.prakash.product_service.dto.PagedResponse;
import com.prakash.product_service.dto.ProductDto;
import com.prakash.product_service.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Product", description = "API for managing products")
public interface ProductApi {

    @Operation(
            summary = "Add a new product",
            description = "Creates a new product with the provided details and returns the unique ID of the created product",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details to be added",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Long.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid product details provided",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    ResponseEntity<Long> saveProduct(@RequestBody ProductDto productDto);

    @Operation(
            summary = "Get product by ID",
            description = "Retrieves detailed information of a product using its unique ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found with the given ID",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    ResponseEntity<ProductDto> getProductById(@PathVariable Long id);

    @Operation(
            summary = "Search products",
            description = "Searches products by name and description using the configured database search index"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PagedResponse.class)
                    )
            )
    })
    ResponseEntity<PagedResponse<ProductDto>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );
}
