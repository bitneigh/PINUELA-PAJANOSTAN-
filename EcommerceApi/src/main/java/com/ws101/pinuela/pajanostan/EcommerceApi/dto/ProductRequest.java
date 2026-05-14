package com.ws101.pinuela.pajanostan.EcommerceApi.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Price must be greater than zero")
    private double price;

    @PositiveOrZero(message = "Stock cannot be negative")
    private int stockQuantity;
}