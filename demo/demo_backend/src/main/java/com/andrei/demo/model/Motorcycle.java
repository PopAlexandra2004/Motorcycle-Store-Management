package com.andrei.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "motorcycle")
public class Motorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Double price;

    @Column(name = "engine_capacity", nullable = true)
    private Integer engineCapacity; // in cubic centimeters (cc)

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity = 0; // default in Java

    @Column(nullable = true)
    private String color; // optional
}
