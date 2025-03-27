package com.andrei.demo.model;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Data
public class PersonUpdateDTO {

    @NotNull(message = " Person ID is required for updating a person.")
    private UUID id; // Needed to identify the person being updated

    @Size(min = 2, max = 100, message = " Name should be between 2 and 100 characters.")
    private String name; // Optional but validated if provided

    @Email(message = " Invalid email format.")
    private String email; // Optional but validated if provided

    @Min(value = 0, message = " Age must be a positive number.")
    private Integer age; // Optional but validated if provided
}
