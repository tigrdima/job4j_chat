package ru.job4j.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Message;

@ThreadSafe
public interface MessageRepository extends CrudRepository<Message, Integer> {
}
