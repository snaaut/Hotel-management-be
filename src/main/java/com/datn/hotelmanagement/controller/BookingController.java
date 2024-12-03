package com.datn.hotelmanagement.controller;

import com.datn.hotelmanagement.dto.request.UpdateBookingReq;
import com.datn.hotelmanagement.dto.response.BookingDto;
import com.datn.hotelmanagement.entity.Booking;
import com.datn.hotelmanagement.enums.BookingStatus;
import com.datn.hotelmanagement.service.BookingService;
import com.datn.hotelmanagement.utils.RevenueComparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/get-booking/{idUser}")
    public List<BookingDto> getBookingsById(@PathVariable Long idUser) {
        return bookingService.getBookingByIdUser(idUser);

    }
    @GetMapping("/getAll")
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping("/create-booking")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        booking.setStatus("Waitting confirm..");
        booking.setPaymentStatus("Unpaid");
        booking.setBookingAt(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy"))));
        Booking createdBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PutMapping("/update-confirm-booking/{id}")
    public ResponseEntity<BookingDto> updateConfirmBooking(@PathVariable Long id, @RequestBody UpdateBookingReq updateBooking) {
        BookingDto updatedBooking = bookingService.updateConfirmBooking(id, updateBooking);
        return ResponseEntity.ok(updatedBooking);
    }
    @PutMapping("/update-return-booking/{id}")
    public ResponseEntity<BookingDto> updateReturnBooking(@PathVariable Long id, @RequestBody UpdateBookingReq updateBooking) {
        BookingDto updatedBooking = bookingService.updateReturnBooking(id, updateBooking);
        return ResponseEntity.ok(updatedBooking);
    }
    @PutMapping("/update-cancel-booking/{id}")
    public ResponseEntity<BookingDto> updateCancelBooking(@PathVariable Long id, @RequestBody UpdateBookingReq updateBooking) {
        BookingDto updatedBooking = bookingService.updateCancelBooking(id, updateBooking);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/revenue")
    public ResponseEntity<Double> calculateMonthlyRevenue(@RequestParam int year, @RequestParam int month) {
        double revenue = bookingService.calculateMonthlyRevenue(year, month);
        return ResponseEntity.ok(revenue);
    }
    @GetMapping("/recent")
    public List<BookingDto> getRecentBookings() {
        return bookingService.getRecentBookings();
    }
    @GetMapping("/revenue-current-month")
    public ResponseEntity<RevenueComparison> getCurrentMonthRevenueComparison() {
        RevenueComparison revenueComparison = bookingService.getCurrentMonthRevenue();
        return ResponseEntity.ok(revenueComparison);
    }
}