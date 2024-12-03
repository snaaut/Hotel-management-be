package com.datn.hotelmanagement.controller;

import com.datn.hotelmanagement.entity.BookedRoom;
import com.datn.hotelmanagement.service.BookedRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booked-room")
public class BookedRoomController {

    private final BookedRoomService bookedRoomService;

    public BookedRoomController(BookedRoomService bookedRoomService) {
        this.bookedRoomService = bookedRoomService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookedRoom> createBookedRoom(@RequestBody BookedRoom bookedRoom) {
        BookedRoom createdBookedRoom = bookedRoomService.createBookedRoom(bookedRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookedRoom);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<List<BookedRoom>> getBookedRoomsByRoomId(@PathVariable Long id) {
        List<BookedRoom> bookedRooms = bookedRoomService.getBookedRoomsByRoomId(id);
        return ResponseEntity.ok(bookedRooms);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<BookedRoom>> getAllBookedRooms() {
        List<BookedRoom> bookedRooms = bookedRoomService.getAllBookedRooms();
        return ResponseEntity.ok(bookedRooms);
    }

    @DeleteMapping("/delete/{idBooking}")
    public ResponseEntity<Void> deleteBookedRoom(@PathVariable Long idBooking) {
        bookedRoomService.deleteBookedRoomByIdBooking(idBooking);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count-booking")
    public ResponseEntity<Long> countBooking() {
        long count = bookedRoomService.countBooking();
        return ResponseEntity.ok(count);
    }
}
