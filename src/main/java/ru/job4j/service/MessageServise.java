package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.model.Message;
import ru.job4j.repository.MessageRepository;
import java.util.Optional;

@ThreadSafe
@Service
public class MessageServise {
    private final MessageRepository messageRepository;

    public MessageServise(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveOrUpdate(Message message) {
        return messageRepository.save(message);
    }

    public Optional<Message> findMessageById(int id) {
        return messageRepository.findById(id);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }
}
