package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.model.Person;

import java.util.Collections;
import java.util.Optional;

@ThreadSafe
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PersonService personService;

    public UserDetailsServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personService.findPersonByPersonName(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return new User(person.get().getPersonName(), person.get().getPassword(), Collections.emptyList());
    }
}
