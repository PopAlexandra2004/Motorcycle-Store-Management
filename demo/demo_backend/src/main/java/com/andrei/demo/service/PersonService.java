package com.andrei.demo.service;

import com.andrei.demo.model.Order;
import com.andrei.demo.model.Person;
import com.andrei.demo.model.PersonCreateDTO;
import com.andrei.demo.model.PersonUpdateDTO;
import com.andrei.demo.repository.OrderRepository;
import com.andrei.demo.repository.PersonRepository;
import com.andrei.demo.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final OrderRepository orderRepository;

    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    public Person getPersonById(UUID uuid) throws EntityNotFoundException {

        return personRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + uuid));
    }

    public Person getPersonByEmail(String email) throws EntityNotFoundException {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with email: " + email));
    }

    @Transactional
    public Person addPerson(PersonCreateDTO personDTO) {
        Person newPerson = new Person();
        newPerson.setName(personDTO.getName());
        newPerson.setEmail(personDTO.getEmail());
        newPerson.setAge(personDTO.getAge());

        return personRepository.save(newPerson);
    }

    @Transactional
    public Person updatePerson(UUID uuid, PersonUpdateDTO personDTO) throws EntityNotFoundException {
        Person existingPerson = personRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + uuid));

        if (personDTO.getName() != null) existingPerson.setName(personDTO.getName());
        if (personDTO.getEmail() != null) existingPerson.setEmail(personDTO.getEmail());
        if (personDTO.getAge() != null) existingPerson.setAge(personDTO.getAge());

        return personRepository.save(existingPerson);
    }

    @Transactional
    public void deletePerson(UUID uuid) throws EntityNotFoundException {
        Person person = personRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + uuid));

        // Get all orders for this person
        List<Order> ordersWithPerson = orderRepository.findByPersonId(uuid);

        // Replace with a dummy "Unknown User"
        Person unknownUser = personRepository.findByEmail("unknown@example.com")
                .orElseGet(() -> {
                    Person p = new Person();
                    p.setName("Unknown User");
                    p.setEmail("unknown@example.com");
                    p.setAge(0);
                    return personRepository.save(p);
                });

        // Update all affected orders
        for (Order order : ordersWithPerson) {
            order.setPerson(unknownUser);
            orderRepository.save(order);
        }

        // Now it's safe to delete
        personRepository.delete(person);
    }
    @Transactional
    @Scheduled(fixedRate = 60000) // once per minute
    public void cleanupDummyPersons() {
        List<Person> personsToDelete = personRepository.findAll()
                .stream()
                .filter(p -> p.getAge() == 0)
                .toList();

        for (Person person : personsToDelete) {
            List<Order> orders = orderRepository.findByPerson(person);

            // Delete all orders tied to this person
            orderRepository.deleteAll(orders);

            // Now that no orders reference the person, we can safely delete them
            personRepository.delete(person);
        }
    }

}