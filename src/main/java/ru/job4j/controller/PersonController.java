package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Person;
import ru.job4j.model.Role;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoleService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_ROLE_PERSON = "ROLE_USER";

    public PersonController(PersonService personService, RoleService roleService,
                            BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.personService = personService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public List<Person> findAllPerson() {
        return personService.findAllPerson();
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Person> findPersonById(@PathVariable int id) {
        Person regPerson = personService.findPersonById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Person is not found"));
        return new ResponseEntity<>(regPerson, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        validationRegistrationData(person);
        person.setPassword(encoder.encode(person.getPassword()));
        person.setRole(roleService.findRoleNamedUser(DEFAULT_ROLE_PERSON).orElse(null));
        return new ResponseEntity<>(
                personService.saveOrUpdate(person), HttpStatus.CREATED
        );
    }

    @PutMapping("/{pId}/updRole/{rId}/")
    public ResponseEntity<Person> updateRolePerson(@PathVariable("pId") int pId, @PathVariable("rId") int rId) {
        Person regPerson = personService.findPersonById(pId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Person is not found"
                ));
        Role role = roleService.findRoleById(rId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Role is not found"
                ));

        regPerson.setRole(role);
        personService.saveOrUpdate(regPerson);
        return ResponseEntity.status(HttpStatus.CREATED).body(regPerson);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<String> deletePerson(@PathVariable int id) {
        Person regPerson = personService.findPersonById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Person is not found"
                ));

            personService.deletePerson(regPerson);
            return ResponseEntity.ok("Person's name - " + regPerson.getPersonName() + " deleted");

    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getLocalizedMessage());
    }

    private void validationRegistrationData(Person person) {
        String personName = person.getPersonName();
        String password = person.getPassword();
        if (personName == null || password == null) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (personService.findPersonByPersonName(personName).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        if (!personName.matches("[a-zA-Z]*")) {
            throw new IllegalArgumentException("The login must consist only of letters");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password length must be more than 8 characters");
        }
    }
}
