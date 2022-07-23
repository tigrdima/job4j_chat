package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Role;
import ru.job4j.service.RoleService;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/")
    public List<Role> findAllRole() {
        return roleService.findAllRole();
    }

    @PostMapping("/")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        if (role.getRole() == null) {
            throw new NullPointerException("Role's name mustn't be empty");
        }
        return new ResponseEntity<>(roleService.saveOrUpdate(role), HttpStatus.CREATED);
    }

    @GetMapping("/name/{name}/")
    public ResponseEntity<Role> findRoleNamedUser(@PathVariable String name) {
        Role regRole = roleService.findRoleNamedUser(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Role not found"
                ));
        return new ResponseEntity<>(regRole, HttpStatus.OK);
    }

    @GetMapping("/id/{id}/")
    public ResponseEntity<Role> findRoleById(@PathVariable int id) {
        Role regRole = roleService.findRoleById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Role not found"
                ));
        return new ResponseEntity<>(regRole, HttpStatus.OK);
    }
}
