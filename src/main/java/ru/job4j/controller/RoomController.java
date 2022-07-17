package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        Optional<Person> regPerson = personService.findPersonById(id);
        if (regPerson.isPresent()) {
            room.getPersons().add(regPerson.get());
            return new ResponseEntity<>(roomService.saveOrUpdate(room), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("{rId}/addPersonId/{pId}/")
    public ResponseEntity<Person> addPersonToRoom(@PathVariable int rId, @PathVariable int pId) {
        Optional<Room> regRoom = roomService.findById(rId);
        Optional<Person> regPerson = personService.findPersonById(pId);
        if (regRoom.isPresent() && regPerson.isPresent()) {
            Room room = regRoom.get();
            Person person = regPerson.get();
            room.getPersons().add(person);
            roomService.saveOrUpdate(room);
            return new ResponseEntity<>(person, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("{rId}/delPersonId/{pId}/")
    public ResponseEntity<Person> deletePersonFromRoom(@PathVariable int rId, @PathVariable int pId) {
        Optional<Room> regRoom = roomService.findById(rId);
        Optional<Person> regPerson = personService.findPersonById(pId);
        if (regRoom.isPresent() && regPerson.isPresent()) {
            Room room = regRoom.get();
            Person person = regPerson.get();
            room.getPersons().remove(person);
            roomService.saveOrUpdate(room);
            return new ResponseEntity<>(person, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{name}/")
    public ResponseEntity<Room> findRoomByName(@PathVariable String name) {
        Optional<Room> regRoom = roomService.findRoomByName(name);
        return new ResponseEntity<>(
                regRoom.orElse(null), regRoom.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @GetMapping("/id/{id}/")
    public ResponseEntity<Room> findRoomById(@PathVariable int id) {
        Optional<Room> regRoom = roomService.findById(id);
        return new ResponseEntity<>(
                regRoom.orElse(null), regRoom.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }
}
