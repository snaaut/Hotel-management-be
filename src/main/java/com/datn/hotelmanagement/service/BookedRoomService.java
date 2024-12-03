package com.datn.hotelmanagement.service;

import com.datn.hotelmanagement.Repository.BookedRoomRepository;
import com.datn.hotelmanagement.Repository.RoomRepository;
import com.datn.hotelmanagement.entity.BookedRoom;
import com.datn.hotelmanagement.entity.Room;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookedRoomService {
    private final BookedRoomRepository bookedRoomRepository;

    public BookedRoomService(BookedRoomRepository bookedRoomRepository, RoomRepository roomRepository) {
        this.bookedRoomRepository = bookedRoomRepository;

    }

    public BookedRoom createBookedRoom(BookedRoom bookedRoom) {
        return bookedRoomRepository.save(bookedRoom);
    }

    public List<BookedRoom> getAllBookedRooms() {
        return bookedRoomRepository.findAll();
    }

    public List<BookedRoom> getBookedRoomsByRoomId(Long roomId) {
        return bookedRoomRepository.findByRoomId(roomId);
    }
    @Transactional
    public void deleteBookedRoomByIdBooking(Long idBooking) {
        bookedRoomRepository.deleteByIdBooking(idBooking);
    }
    public long countBooking() {
        return bookedRoomRepository.count();
    }
}
