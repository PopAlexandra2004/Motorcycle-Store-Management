package com.andrei.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class OrderedMotorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "motorcycle_id")
    private Motorcycle motorcycle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @Min(value = 1, message = "Quantity must be at least 1.")
    private int quantity;
}
