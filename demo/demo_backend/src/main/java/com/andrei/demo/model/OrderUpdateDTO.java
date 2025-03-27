package com.andrei.demo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderUpdateDTO {

    @NotNull(message = " Order ID is required.")
    private UUID id;

    @NotNull(message = " Person ID is required.")
    private UUID personId;

    @NotEmpty(message = " At least one motorcycle must be selected.")
    private List<OrderedMotorcycleDTO> motorcycles;

    @NotNull(message = " Date must be provided.")
    private LocalDateTime date;

    // totalCost will be calculated in backend based on motorcycle price * quantity
}
