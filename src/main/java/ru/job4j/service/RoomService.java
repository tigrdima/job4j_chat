package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.job4j.model.Room;
import ru.job4j.repository.RoomRepository;

import java.util.*;

@ThreadSafe
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Set<Room> findAllRooms() {
        List<Room> roomList = new ArrayList<>();
        roomRepository.findAll().forEach(roomList::add);
        return new HashSet<>(roomList);
    }

    public Room saveOrUpdate(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> findRoomByName(String room) {
        return Optional.ofNullable(roomRepository.findByName(room));
    }

    public Optional<Room> findById(int id) {
        return Optional.ofNullable(roomRepository.findById(id));
    }

}
