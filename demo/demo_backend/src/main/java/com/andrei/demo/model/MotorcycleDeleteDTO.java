package com.andrei.demo.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MotorcycleDeleteDTO {

    @NotNull(message = "Motorcycle ID is required for deletion.")
    private UUID id;
}
