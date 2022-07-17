package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return new ResponseEntity<>(roleService.saveOrUpdate(role), HttpStatus.CREATED);
    }

    @GetMapping("/name/{name}/")
    public ResponseEntity<Role> findRoleNamedUser(@PathVariable String name) {
        Optional<Role> regRole = roleService.findRoleNamedUser(name);
        return new ResponseEntity<>(
                regRole.orElse(null), regRole.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @GetMapping("/id/{id}/")
    public ResponseEntity<Role> findRoleById(@PathVariable int id) {
        Optional<Role> regRole = roleService.findRoleById(id);
        return new ResponseEntity<>(
                regRole.orElse(null), regRole.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }
}
