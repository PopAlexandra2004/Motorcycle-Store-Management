package com.andrei.demo.model;

import com.andrei.demo.model.OrderedMotorcycleDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderCreateDTO {
    @NotNull(message = "Person must be selected.")
    private UUID personId;

    @NotEmpty(message = "At least one motorcycle must be selected.")
    private List<OrderedMotorcycleDTO> motorcycles;

    @NotNull(message = "Date must be provided.")
    private LocalDateTime date;
}

