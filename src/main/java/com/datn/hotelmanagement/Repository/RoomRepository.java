package com.datn.hotelmanagement.Repository;


import com.datn.hotelmanagement.entity.BookedRoom;
import com.datn.hotelmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
