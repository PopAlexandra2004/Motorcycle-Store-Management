package com.andrei.demo.controller;

import com.andrei.demo.exception.EntityNotFoundException;
import com.andrei.demo.model.PersonCreateDTO;
import com.andrei.demo.model.PersonUpdateDTO;
import com.andrei.demo.service.PersonService;
import com.andrei.demo.model.Person;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
public class PersonController {
    private final PersonService personService;

    @GetMapping("/person")
    public List<Person> getPeople() {
        return personService.getPeople();
    }

    @GetMapping("/person/{uuid}")
    public Person getPersonById(@PathVariable UUID uuid) throws EntityNotFoundException {
        return personService.getPersonById(uuid);
    }

    @GetMapping("/person/email/{email}")
    public Person getPersonByEmail(@PathVariable String email) throws EntityNotFoundException {
        return personService.getPersonByEmail(email);
    }

    @PostMapping("/person")
    public Person addPerson(@Valid @RequestBody PersonCreateDTO personDTO) {
        return personService.addPerson(personDTO);
    }

    @PutMapping("/person/{uuid}")
    public Person updatePerson(@PathVariable UUID uuid, @RequestBody PersonUpdateDTO personDTO) throws EntityNotFoundException {
        return personService.updatePerson(uuid, personDTO);
    }

    // ✅ ADD THIS PATCH METHOD TO SUPPORT PARTIAL UPDATES
    @PatchMapping("/person/{uuid}")
    public Person patchPerson(@PathVariable UUID uuid, @RequestBody PersonUpdateDTO personDTO) throws EntityNotFoundException {
        return personService.updatePerson(uuid, personDTO);
    }

    @DeleteMapping("/person/{uuid}")
    public void deletePerson(@PathVariable UUID uuid) throws EntityNotFoundException {
        personService.deletePerson(uuid);
    }
    @DeleteMapping("/cleanup")
    public ResponseEntity<String> triggerCleanup() {
        personService.cleanupDummyPersons();
        return ResponseEntity.ok(" Dummy users with age 0 deleted.");
    }


}
