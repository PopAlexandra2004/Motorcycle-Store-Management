package com.andrei.demo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderedMotorcycleDTO {

    @NotNull(message = "Motorcycle ID must be provided.")
    private UUID motorcycleId;

    @Min(value = 1, message = "Quantity must be at least 1.")
    private int quantity;
}
