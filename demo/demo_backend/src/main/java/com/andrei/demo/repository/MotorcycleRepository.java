package com.andrei.demo.repository;

import com.andrei.demo.model.Motorcycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, UUID> {

    //  Find all motorcycles by brand
    List<Motorcycle> findByBrand(String brand);

    //  Find a motorcycle by exact brand and model
    Optional<Motorcycle> findByBrandAndModel(String brand, String model);

    //  Find motorcycles in a specific price range
    List<Motorcycle> findByPriceBetween(Double minPrice, Double maxPrice);

    // Approximate brand match (start or end)
    @Query("SELECT m FROM Motorcycle m WHERE m.brand LIKE ?1% OR m.brand LIKE %?1")
    List<Motorcycle> findByBrandApproximate(String brand);

    // Find motorcycles whose model starts or ends with a given value
    List<Motorcycle> findByModelStartingWithOrModelEndingWith(String start, String end);

    // Find motorcycles newer than a specific year
    List<Motorcycle> findByYearGreaterThan(Integer year);

    // Optional: Find all by engine capacity >= X (based on frontend model)
    List<Motorcycle> findByEngineCapacityGreaterThanEqual(Integer capacity);

    // Optional: Find motorcycles by optional color
    List<Motorcycle> findByColor(String color);
}
