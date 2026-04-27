package com.prakash.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request and Response object for Product details")
public class ProductDto {
    @Schema(description = "Name of the product",  requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Detailed description of the product")
    private String description;

    @Schema(description = "Price of the product in USD", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Quantity available in stock", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;
}
