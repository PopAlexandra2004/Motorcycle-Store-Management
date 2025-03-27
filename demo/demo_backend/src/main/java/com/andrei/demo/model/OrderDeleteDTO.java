package com.andrei.demo.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderDeleteDTO {

    @NotNull(message = " Order ID is required.")
    private UUID id;
}
