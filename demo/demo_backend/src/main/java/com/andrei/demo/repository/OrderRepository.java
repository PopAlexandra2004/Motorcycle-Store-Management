package com.andrei.demo.repository;

import com.andrei.demo.model.Motorcycle;
import com.andrei.demo.model.Order;
import com.andrei.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByPersonId(UUID personId);

    List<Order> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Optional<Order> findTopByPersonIdOrderByDateDesc(UUID personId);

    @Query("SELECT o FROM Order o JOIN o.motorcycles om WHERE om.motorcycle.id = :motorcycleId")
    List<Order> findByMotorcycleId(@Param("motorcycleId") UUID motorcycleId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.motorcycles om LEFT JOIN FETCH om.motorcycle WHERE o.person.id = :personId")
    List<Order> findOrdersWithMotorcyclesByPerson(@Param("personId") UUID personId);
    List<Order> findByPerson(Person person);
    List<Order> findByMotorcyclesMotorcycle(Motorcycle motorcycle);


}
