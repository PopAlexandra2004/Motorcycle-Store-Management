package com.andrei.demo.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

@Data
public class PersonDeleteDTO {

    @NotNull(message = "Person ID is required.")
    private UUID id;
}
