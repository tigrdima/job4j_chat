package ru.job4j.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.model.Role;

@ThreadSafe
public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query("select r from Role r where r.role = :rRole")
    Role findRoleUser(@Param("rRole") String role);
}
