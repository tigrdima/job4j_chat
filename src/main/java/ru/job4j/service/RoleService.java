package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.model.Role;
import ru.job4j.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role saveOrUpdate(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> findAllRole() {
        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    public Optional<Role> findRoleNamedUser(String role) {
        return Optional.ofNullable(roleRepository.findRoleUser(role));
    }

    public Optional<Role> findRoleById(int id) {
        return roleRepository.findById(id);
    }

}
