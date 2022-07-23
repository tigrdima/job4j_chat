package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Message;
import ru.job4j.model.Person;
import ru.job4j.model.Room;
import ru.job4j.service.MessageServise;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@ThreadSafe
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageServise messageServise;
    private final PersonService personService;
    private final RoomService roomService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class.getSimpleName());
    private final ObjectMapper objectMapper;

    public MessageController(MessageServise messageServise, PersonService personService, RoomService roomService, ObjectMapper objectMapper) {
        this.messageServise = messageServise;
        this.personService = personService;
        this.roomService = roomService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/roomId/{rId}/personId/{pId}/")
    public ResponseEntity<Message> createMessage(@RequestBody Message message, @PathVariable int rId, @PathVariable int pId) {
        if (message.getDescription() == null) {
            throw new NullPointerException("Message mustn't be empty");
        }
        Room regRoom = roomService.findById(rId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Room is not found."
                        ));
        Person regPerson = personService.findPersonById(pId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Person is not found"
                ));

            message.setPerson(regPerson);
            regRoom.getMessages().add(message);
            return new ResponseEntity<>(messageServise.saveOrUpdate(message), HttpStatus.CREATED);
    }

    @DeleteMapping("{mId}/personId/{pId}/")
    public ResponseEntity<Void> delMessageByPersonId(@PathVariable int mId, @PathVariable int pId) {
        Message regMessage = messageServise.findMessageById(mId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Message is not found."));
        Person regPerson = personService.findPersonById(pId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Person is not found"
                ));

        if (!regMessage.getPerson().getPersonName().equals(regPerson.getPersonName())) {
          throw new IllegalArgumentException("Message does not match the user");
        }

        messageServise.deleteMessage(regMessage);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }
}
