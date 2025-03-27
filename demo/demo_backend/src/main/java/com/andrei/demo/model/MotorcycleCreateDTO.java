package com.andrei.demo.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MotorcycleCreateDTO {

    @NotBlank(message = "Brand is required.")
    private String brand;

    @NotBlank(message = "Model is required.")
    private String model;

    @NotNull(message = "Year is required.")
    @Min(value = 1900, message = "Year must be valid.")
    @Max(value = 2025, message = "Year must be less than or equal to 2025.")
    private Integer year;

    @NotNull(message = "Price is required.")
    @Min(value = 0, message = "Price must be non-negative.")
    private Double price;

    @NotNull(message = "Engine capacity is required.")
    @Min(value = 50, message = "Engine capacity must be at least 50cc.")
    private Integer engineCapacity;

    @NotNull(message = "Available Quantity is required.")
    @Min(value = 0, message = "Available Quantity must be non-negative.")
    private int availableQuantity;

    private String color; // optional
}
