package com.andrei.demo.service;

import com.andrei.demo.model.*;
import com.andrei.demo.repository.MotorcycleRepository;
import com.andrei.demo.repository.OrderRepository;
import com.andrei.demo.repository.PersonRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import com.andrei.demo.exception.EntityNotFoundException;
import com.andrei.demo.exception.InsufficientStockException;
import com.andrei.demo.exception.InvalidOrderException;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PersonRepository personRepository;
    private final MotorcycleRepository motorcycleRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order with id " + id + " not found"));
    }

    @Transactional
    public Order addOrder(@Valid OrderCreateDTO dto) {
        try {
            if (dto.getMotorcycles() == null || dto.getMotorcycles().isEmpty()) {
                throw new InvalidOrderException("At least one motorcycle must be selected.");
            }

            Person person = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new EntityNotFoundException("Person not found with ID: " + dto.getPersonId()));

            List<OrderedMotorcycle> orderedMotorcycles = new ArrayList<>();
            double totalCost = 0;

            for (OrderedMotorcycleDTO orderedDTO : dto.getMotorcycles()) {
                Motorcycle motorcycle = motorcycleRepository.findById(orderedDTO.getMotorcycleId())
                        .orElseThrow(() -> new NoSuchElementException("Motorcycle with id " + orderedDTO.getMotorcycleId() + " not found"));
                int quantity = orderedDTO.getQuantity();

                if (motorcycle.getAvailableQuantity() < quantity) {
                    throw new InsufficientStockException("Only " + motorcycle.getAvailableQuantity() +
                            " units available for " + motorcycle.getBrand() + " " + motorcycle.getModel());
                }
                motorcycle.setAvailableQuantity(motorcycle.getAvailableQuantity() - quantity);
                motorcycleRepository.save(motorcycle);

                OrderedMotorcycle orderedMotorcycle = new OrderedMotorcycle();
                orderedMotorcycle.setMotorcycle(motorcycle);
                orderedMotorcycle.setQuantity(quantity);
                orderedMotorcycle.setOrder(null);
                orderedMotorcycles.add(orderedMotorcycle);

                totalCost += motorcycle.getPrice() * quantity;
            }

            Order order = new Order();
            order.setPerson(person);
            order.setMotorcycles(new ArrayList<>());
            order.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
            order.setTotalCost(totalCost);

            Order savedOrder = orderRepository.save(order);

            for (OrderedMotorcycle om : orderedMotorcycles) {
                om.setOrder(savedOrder);
            }

            savedOrder.setMotorcycles(orderedMotorcycles);
            return orderRepository.save(savedOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to process order: " + e.getMessage());
        }
    }

    @Transactional
    public Order updateOrder(UUID id, @Valid OrderUpdateDTO dto) throws  EntityNotFoundException, InsufficientStockException {
        Order existing = getOrderById(id);

        if (dto.getPersonId() != null) {
            Person person = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new EntityNotFoundException("Person not found with ID: " + dto.getPersonId()));
            existing.setPerson(person);
        }

        if (dto.getDate() != null) {
            existing.setDate(dto.getDate());
        }

        if (dto.getMotorcycles() != null) {
            // Restore quantities from existing motorcycles
            for (OrderedMotorcycle old : existing.getMotorcycles()) {
                Motorcycle oldMoto = old.getMotorcycle();
                oldMoto.setAvailableQuantity(oldMoto.getAvailableQuantity() + old.getQuantity());
                motorcycleRepository.save(oldMoto);
            }

            // ✅ Clear the current list safely (triggers orphan removal)
            existing.getMotorcycles().clear();
            orderRepository.save(existing); // Save to update orphan state

            double totalCost = 0;

            for (OrderedMotorcycleDTO omDTO : dto.getMotorcycles()) {
                Motorcycle motorcycle = motorcycleRepository.findById(omDTO.getMotorcycleId())
                        .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found with ID: " + omDTO.getMotorcycleId()));

                if (motorcycle.getAvailableQuantity() < omDTO.getQuantity()) {
                    throw new InsufficientStockException("Only " + motorcycle.getAvailableQuantity() +
                            " units available for " + motorcycle.getBrand() + " " + motorcycle.getModel());
                }

                // Decrease available quantity
                motorcycle.setAvailableQuantity(motorcycle.getAvailableQuantity() - omDTO.getQuantity());
                motorcycleRepository.save(motorcycle);

                //Create new OrderedMotorcycle
                OrderedMotorcycle om = new OrderedMotorcycle();
                om.setMotorcycle(motorcycle);
                om.setQuantity(omDTO.getQuantity());
                om.setOrder(existing);

                existing.getMotorcycles().add(om);
                totalCost += motorcycle.getPrice() * omDTO.getQuantity(); // Update total cost
            }

            existing.setTotalCost(totalCost); // Set new total cost
        }

        return orderRepository.save(existing);
    }


    @Transactional
    public Order patchOrder(UUID id, OrderUpdateDTO dto) throws EntityNotFoundException, InsufficientStockException {
        Order existing = getOrderById(id);

        // Restore quantities
        for (OrderedMotorcycle oldOM : existing.getMotorcycles()) {
            Motorcycle oldMoto = oldOM.getMotorcycle();
            oldMoto.setAvailableQuantity(oldMoto.getAvailableQuantity() + oldOM.getQuantity());
            motorcycleRepository.save(oldMoto);
        }

        if (dto.getPersonId() != null) {
            Person person = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new EntityNotFoundException(" Person not found with ID: " + dto.getPersonId()));
            existing.setPerson(person);
        }

        if (dto.getDate() != null) {
            existing.setDate(dto.getDate());
        }

        if (dto.getMotorcycles() != null) {
            List<OrderedMotorcycle> updatedList = new ArrayList<>();
            double totalCost = 0;

            for (OrderedMotorcycleDTO omDTO : dto.getMotorcycles()) {
                Motorcycle motorcycle = motorcycleRepository.findById(omDTO.getMotorcycleId())
                        .orElseThrow(() -> new EntityNotFoundException(" Motorcycle not found with ID: " + omDTO.getMotorcycleId()));

                if (motorcycle.getAvailableQuantity() < omDTO.getQuantity()) {
                    throw new InsufficientStockException(" Only " + motorcycle.getAvailableQuantity() +
                            " units available for " + motorcycle.getBrand() + " " + motorcycle.getModel());
                }
                motorcycle.setAvailableQuantity(motorcycle.getAvailableQuantity() - omDTO.getQuantity());
                motorcycleRepository.save(motorcycle);

                OrderedMotorcycle om = new OrderedMotorcycle();
                om.setMotorcycle(motorcycle);
                om.setQuantity(omDTO.getQuantity());
                om.setOrder(existing);

                updatedList.add(om);
                totalCost += motorcycle.getPrice() * omDTO.getQuantity();
            }

            existing.setMotorcycles(updatedList);
            existing.setTotalCost(totalCost);
        }

        return orderRepository.save(existing);
    }

    @Transactional
    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order with id " + id + " does not exist"));

        for (OrderedMotorcycle om : order.getMotorcycles()) {
            Motorcycle moto = om.getMotorcycle();
            moto.setAvailableQuantity(moto.getAvailableQuantity() + om.getQuantity());
            motorcycleRepository.save(moto);
        }

        orderRepository.delete(order);
    }
}
