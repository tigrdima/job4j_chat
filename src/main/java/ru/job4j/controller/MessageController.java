package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Message;
import ru.job4j.model.Person;
import ru.job4j.model.Room;
import ru.job4j.service.MessageServise;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;

import java.util.Optional;

@ThreadSafe
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageServise messageServise;
    private final PersonService personService;
    private final RoomService roomService;

    public MessageController(MessageServise messageServise, PersonService personService, RoomService roomService) {
        this.messageServise = messageServise;
        this.personService = personService;
        this.roomService = roomService;
    }

    @PostMapping("/roomId/{rId}/personId/{pId}/")
    public ResponseEntity<Message> createMessage(@RequestBody Message message, @PathVariable int rId, @PathVariable int pId) {
        Optional<Room> regRoom = roomService.findById(rId);
        Optional<Person> regPerson = personService.findPersonById(pId);

        if (regRoom.isPresent() && regPerson.isPresent()) {
            Room room = regRoom.get();
            message.setPerson(regPerson.get());
            room.getMessages().add(message);
            return new ResponseEntity<>(messageServise.saveOrUpdate(message), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{mId}/personId/{pId}/")
    public ResponseEntity<Void> delMessageByPersonId(@PathVariable int mId, @PathVariable int pId) {
        Optional<Person> regPerson = personService.findPersonById(pId);
        Optional<Message> regMessage = messageServise.findMessageById(mId);
        if (regMessage.isPresent() && regPerson.isPresent()) {
            messageServise.deleteMessage(regMessage.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
