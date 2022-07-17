package ru.job4j.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.model.Room;

@ThreadSafe
public interface RoomRepository extends CrudRepository<Room, Integer> {
    @Override
    @Query("select distinct r from Room r join fetch r.persons p join fetch p.role join fetch r.messages m "
            + "join fetch m.person p1 join fetch p1.role")
    Iterable<Room> findAll();

    @Query("select distinct r from Room r join fetch r.persons p join fetch p.role join fetch r.messages m "
            + "join fetch m.person p1 join fetch p1.role where r.roomName = :rRoomName")
    Room findByName(@Param("rRoomName") String room);

    @Query("select distinct r from Room r join fetch r.persons p join fetch p.role join fetch r.messages m "
            + "join fetch m.person p1 join fetch p1.role where r.id = :rId")
    Room findById(@Param("rId") int id);
}
