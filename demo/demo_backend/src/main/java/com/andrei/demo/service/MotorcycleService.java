package com.andrei.demo.service;

import com.andrei.demo.model.*;
import com.andrei.demo.repository.MotorcycleRepository;
import com.andrei.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.andrei.demo.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MotorcycleService {
    private final MotorcycleRepository motorcycleRepository;
    private final OrderRepository orderRepository;

    public List<Motorcycle> getAllMotorcycles() {
        return motorcycleRepository.findAll();
    }

    public Motorcycle getMotorcycleById(UUID id) throws EntityNotFoundException {
        return motorcycleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle with ID " + id + " not found"));

    }
    @Transactional
    public Motorcycle addMotorcycle(@Valid MotorcycleCreateDTO dto) {
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setBrand(dto.getBrand());
        motorcycle.setModel(dto.getModel());
        motorcycle.setYear(dto.getYear());
        motorcycle.setPrice(dto.getPrice());
        motorcycle.setEngineCapacity(dto.getEngineCapacity());
        motorcycle.setAvailableQuantity(dto.getAvailableQuantity());
        motorcycle.setColor(dto.getColor());

        return motorcycleRepository.save(motorcycle);
    }

    public Motorcycle updateMotorcycle(UUID id, @Valid MotorcycleUpdateDTO dto) throws EntityNotFoundException {
        Motorcycle motorcycle = getMotorcycleById(id);

        motorcycle.setBrand(dto.getBrand());
        motorcycle.setModel(dto.getModel());
        motorcycle.setYear(dto.getYear());
        motorcycle.setPrice(dto.getPrice());
        motorcycle.setEngineCapacity(dto.getEngineCapacity());
        motorcycle.setAvailableQuantity(dto.getAvailableQuantity());
        motorcycle.setColor(dto.getColor());

        return motorcycleRepository.save(motorcycle);
    }
    @Transactional
    public Motorcycle patchMotorcycle(UUID id, MotorcycleUpdateDTO dto) throws EntityNotFoundException {
        Motorcycle motorcycle = getMotorcycleById(id);

        if (dto.getBrand() != null) motorcycle.setBrand(dto.getBrand());
        if (dto.getModel() != null) motorcycle.setModel(dto.getModel());
        if (dto.getYear() != null) motorcycle.setYear(dto.getYear());
        if (dto.getPrice() != null) motorcycle.setPrice(dto.getPrice());
        if (dto.getEngineCapacity() != null) motorcycle.setEngineCapacity(dto.getEngineCapacity());
        if (dto.getAvailableQuantity() != null) motorcycle.setAvailableQuantity(dto.getAvailableQuantity());
        if (dto.getColor() != null) motorcycle.setColor(dto.getColor());

        return motorcycleRepository.save(motorcycle);
    }

    @Transactional
    public void deleteMotorcycle(UUID id) throws EntityNotFoundException {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle with ID " + id + " not found"));


        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            boolean containsMotorcycle = order.getMotorcycles().stream()
                    .anyMatch(om -> om.getMotorcycle() != null && om.getMotorcycle().getId().equals(id));

            if (containsMotorcycle) {
                orderRepository.delete(order); // delete the entire order
            }
        }

        motorcycleRepository.delete(motorcycle); // now it's safe to delete the motorcycle
    }



    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupUnavailableMotorcycles() {
        List<Motorcycle> motorcyclesToDelete = motorcycleRepository.findAll()
                .stream()
                .filter(m -> m.getAvailableQuantity() == 0)
                .toList();

        for (Motorcycle motorcycle : motorcyclesToDelete) {
            List<Order> affectedOrders = orderRepository.findByMotorcyclesMotorcycle(motorcycle);
            for (Order order : affectedOrders) {
                List<OrderedMotorcycle> updatedList = order.getMotorcycles().stream()
                        .filter(om -> !om.getMotorcycle().getId().equals(motorcycle.getId()))
                        .toList();

                if (updatedList.isEmpty()) {
                    orderRepository.delete(order);
                } else {
                    order.setMotorcycles(new ArrayList<>(updatedList)); // 🔧 Fix applied here too
                    double newTotal = updatedList.stream()
                            .mapToDouble(om -> om.getMotorcycle().getPrice() * om.getQuantity())
                            .sum();
                    order.setTotalCost(newTotal);
                    orderRepository.save(order);
                }
            }

            motorcycleRepository.delete(motorcycle);
        }
    }

}