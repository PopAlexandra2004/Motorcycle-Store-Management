package com.andrei.demo.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MotorcycleUpdateDTO {
    @NotBlank(message = "Brand is required.")
    private String brand;

    @NotBlank(message = "Model is required.")
    private String model;

    @Min(value = 1900, message = "we don't sell antiques.")
    @Max(value = 2025, message = "we do not sell from the future")
    private Integer year;

    @Positive(message = "Price must be a positive number.")
    private Double price;

    @Min(value = 50, message = "Engine capacity must be at least 50cc.")
    private Integer engineCapacity;

    @NotNull(message = "Available Quantity is required.")
    @Min(value = 0, message = "Available Quantity must be non-negative.")
    private Integer availableQuantity;

    private String color;
}
