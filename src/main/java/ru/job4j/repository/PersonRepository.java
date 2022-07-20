package ru.job4j.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.model.Person;
import java.util.Optional;

@ThreadSafe
public interface PersonRepository extends CrudRepository<Person, Integer> {
    @Override
    @Query("select distinct p from Person p join fetch p.role")
    Iterable<Person> findAll();

    @Query("select distinct p from Person p join fetch p.role where p.id = :pId")
    Optional<Person> findById(@Param("pId") int id);

    @Query("select distinct p from Person p join fetch p.role where p.personName = :pName")
    Optional<Person> findByName(@Param("pName") String personName);
}
