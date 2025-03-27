package com.andrei.demo.controller;

import com.andrei.demo.exception.EntityNotFoundException;
import com.andrei.demo.exception.InsufficientStockException;
import com.andrei.demo.model.Order;
import com.andrei.demo.model.OrderCreateDTO;
import com.andrei.demo.model.OrderUpdateDTO;
import com.andrei.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final OrderService orderService;

    /**
     * Retrieve all orders.
     */
    @GetMapping("/order")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Retrieve a single order by ID.
     */
    @GetMapping("/order/{id}")
    public Order getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    /**
     * Create a new order.
     */
    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public Order addOrder(@Valid @RequestBody OrderCreateDTO orderDTO) {
        return orderService.addOrder(orderDTO);
    }

    /**
     * Fully update an existing order.
     */
    @PutMapping("/order/{id}")
    public Order updateOrder(
            @PathVariable UUID id,
            @Valid @RequestBody OrderUpdateDTO orderDTO
    ) throws InsufficientStockException, EntityNotFoundException {
        return orderService.updateOrder(id, orderDTO);
    }

    /**
     * Partially update an existing order.
     */
    @PatchMapping("/order/{id}")
    public Order patchOrder(
            @PathVariable UUID id,
            @RequestBody OrderUpdateDTO orderDTO
    ) throws InsufficientStockException, EntityNotFoundException {
        return orderService.patchOrder(id, orderDTO);
    }

    /**
     * Delete an order by ID.
     */
    @DeleteMapping("/order/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
    }


}
