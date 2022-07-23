package ru.job4j.model;

import net.jcip.annotations.ThreadSafe;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@ThreadSafe
@Entity
@Table(name = "person", uniqueConstraints = {@UniqueConstraint(columnNames = "personName")})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String personName;
    private String password;
    private Timestamp created = new Timestamp(System.currentTimeMillis());

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id && personName.equals(person.personName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personName);
    }
}
