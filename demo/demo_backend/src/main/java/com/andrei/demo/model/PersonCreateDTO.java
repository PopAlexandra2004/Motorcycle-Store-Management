package com.andrei.demo.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonCreateDTO {

    @NotBlank(message = "👤 Name is required.")
    @Size(min = 2, max = 100, message = "📏 Name should be between 2 and 100 characters.")
    private String name;

    @NotNull(message = " Age is required.")
    @Min(value = 16, message = " Age must be at least 18.")
    @Max(value = 100, message = " Age must not exceed 100.")
    private Integer age;

    @NotBlank(message = " Email is required.")
    @Email(message = " Invalid email format. Use a valid email (e.g., example@domain.com).")
    private String email;
}
