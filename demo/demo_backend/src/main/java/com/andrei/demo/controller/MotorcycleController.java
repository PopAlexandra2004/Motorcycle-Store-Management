package com.andrei.demo.controller;

import com.andrei.demo.exception.EntityNotFoundException;
import com.andrei.demo.model.MotorcycleCreateDTO;
import com.andrei.demo.model.MotorcycleUpdateDTO;
import com.andrei.demo.model.Motorcycle;
import com.andrei.demo.service.MotorcycleService;
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
@CrossOrigin
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    // ✅ Get All Motorcycles
    @GetMapping("/motorcycle")
    public List<Motorcycle> getAllMotorcycles() {
        return motorcycleService.getAllMotorcycles();
    }

    // ✅ Get Motorcycle by ID
    @GetMapping("/motorcycle/{id}")
    public Motorcycle getMotorcycleById(@PathVariable UUID id) throws EntityNotFoundException {
        return motorcycleService.getMotorcycleById(id);
    }

    // ✅ Create Motorcycle
    @PostMapping("/motorcycle")
    public Motorcycle addMotorcycle(@Valid @RequestBody MotorcycleCreateDTO motorcycleDTO) {
        return motorcycleService.addMotorcycle(motorcycleDTO);
    }

    // ✅ Full Update (PUT)
    @PutMapping("/motorcycle/{id}")
    public Motorcycle updateMotorcycle(
            @PathVariable UUID id,
            @Valid @RequestBody MotorcycleUpdateDTO motorcycleDTO
    ) throws EntityNotFoundException {
        return motorcycleService.updateMotorcycle(id, motorcycleDTO);
    }

    // ✅ Partial Update (PATCH)
    @PatchMapping("/motorcycle/{id}")
    public Motorcycle patchMotorcycle(
            @PathVariable UUID id,
            @RequestBody MotorcycleUpdateDTO motorcycleDTO
    ) throws EntityNotFoundException {
        return motorcycleService.patchMotorcycle(id, motorcycleDTO);
    }

    // ✅ Delete Motorcycle by ID
    @DeleteMapping("/motorcycle/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMotorcycle(@PathVariable UUID id) throws EntityNotFoundException {
        motorcycleService.deleteMotorcycle(id);
    }
}
