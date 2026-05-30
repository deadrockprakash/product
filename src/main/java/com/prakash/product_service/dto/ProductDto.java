package com.prakash.product_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    @NotBlank(message = "Product name is required")
    private String name;

    @Schema(description = "Detailed description of the product")
    private String description;

    @Schema(description = "Price of the product in USD", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than zero")
    private BigDecimal price;

    @Schema(description = "Quantity available in stock", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product quantity is required")
    @PositiveOrZero(message = "Product quantity cannot be negative")
    private Integer quantity;
}
