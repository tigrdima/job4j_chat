package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.model.Person;
import ru.job4j.model.Room;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;
import java.util.Optional;
import java.util.Set;

@ThreadSafe
@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    private final PersonService personService;

    public RoomController(RoomService roomService, PersonService personService) {
        this.roomService = roomService;
        this.personService = personService;
    }

    @GetMapping("/")
    public Set<Room> findAllRooms() {
        return roomService.findAllRooms();
    }

    @PostMapping("/personId/{pId}/")
    public ResponseEntity<Room> createRoom(@PathVariable("pId") int id, @RequestBody Room room) {
        if (room.getRoomName() == null) {
            throw new NullPointerException("Room's name mustn't be empty");
        }
        Optional<Person> regPerson = personService.findPersonById(id);
        if (regPerson.isPresent()) {
            room.getPersons().add(regPerson.get());
            return new ResponseEntity<>(roomService.saveOrUpdate(room), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("{rId}/addPersonId/{pId}/")
    public ResponseEntity<Person> addPersonToRoom(@PathVariable int rId, @PathVariable int pId) {
        Room regRoom = roomService.findById(rId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room is not found"
                ));
        Person regPerson = personService.findPersonById(pId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Room is not found"
                ));

            regRoom.getPersons().add(regPerson);
            roomService.saveOrUpdate(regRoom);
            return new ResponseEntity<>(regPerson, HttpStatus.CREATED);
    }

    @PostMapping("{rId}/delPersonId/{pId}/")
    public ResponseEntity<Person> deletePersonFromRoom(@PathVariable int rId, @PathVariable int pId) {
        Room regRoom = roomService.findById(rId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room is not found"
                ));
        Person regPerson = personService.findPersonById(pId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Room is not found"
                ));
        if (!regRoom.getPersons().contains(regPerson)) {
            ResponseEntity.notFound().build();
        }
            regRoom.getPersons().remove(regPerson);
            roomService.saveOrUpdate(regRoom);
            return new ResponseEntity<>(regPerson, HttpStatus.CREATED);
    }

    @GetMapping("/name/{name}/")
    public ResponseEntity<Room> findRoomByName(@PathVariable String name) {
        Room regRoom = roomService.findRoomByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Room is not found"
                ));
        return new ResponseEntity<>(regRoom, HttpStatus.OK);
    }

    @GetMapping("/id/{id}/")
    public ResponseEntity<Room> findRoomById(@PathVariable int id) {
        Room regRoom = roomService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Room is not found"
                ));
        return new ResponseEntity<>(regRoom, HttpStatus.OK);
    }
}
