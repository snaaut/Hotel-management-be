package com.datn.hotelmanagement.Repository;

import com.datn.hotelmanagement.entity.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {
    List<BookedRoom> findByRoomId(Long roomId);
    void deleteByIdBooking(Long idBooking);
    List<BookedRoom> findByIdBooking(Long idBooking);
}
