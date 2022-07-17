package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Person;
import ru.job4j.model.Role;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoleService;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final RoleService roleService;

    private static final String DEFAULT_ROLE_PERSON = "ROLE_USER";

    public PersonController(PersonService personService, RoleService roleService) {
        this.personService = personService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public List<Person> findAllPerson() {
        return personService.findAllPerson();
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Person> findPersonById(@PathVariable int id) {
        Optional<Person> regPerson = personService.findPersonById(id);
        return new ResponseEntity<>(regPerson.orElse(null),
                regPerson.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        person.setRole(roleService.findRoleNamedUser(DEFAULT_ROLE_PERSON).orElse(null));
        return new ResponseEntity<>(
                personService.saveOrUpdate(person), HttpStatus.CREATED
        );
    }

    @PutMapping("/{pId}/updRole/{rId}/")
    public ResponseEntity<Void> updateRolePerson(@PathVariable("pId") int pId, @PathVariable("rId") int rId) {
        Optional<Person> regPerson = personService.findPersonById(pId);
        if (regPerson.isPresent()) {
            Person person = regPerson.get();
            Role role = roleService.findRoleById(rId).orElse(null);
            person.setRole(role);
            personService.saveOrUpdate(person);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        Optional<Person> regPerson = personService.findPersonById(id);
        if (regPerson.isPresent()) {
            Person person = regPerson.get();
            personService.deletePerson(person);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
