package com.datn.hotelmanagement.service;

import com.datn.hotelmanagement.Repository.AccountRepo;
import com.datn.hotelmanagement.Repository.BookingRepository;
import com.datn.hotelmanagement.Repository.RoomRepository;
import com.datn.hotelmanagement.dto.request.UpdateBookingReq;
import com.datn.hotelmanagement.dto.response.BookingDto;
import com.datn.hotelmanagement.entity.Booking;
import com.datn.hotelmanagement.utils.RevenueComparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final AccountRepo userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, AccountRepo userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Booking createBooking(Booking booking) {

        Booking createdBooking = bookingRepository.save(booking);
        return createdBooking;
    }


    public BookingDto updateConfirmBooking(Long id, UpdateBookingReq updateBooking) {
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        existingBooking.setStatus(updateBooking.getStatus());
        existingBooking.setConfirmAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy")));
//        existingBooking.setReturnAt(updateBooking.getReturnAt());
        Booking updatedBooking = bookingRepository.save(existingBooking);
        return convertToDTO(updatedBooking);
    }
    public BookingDto updateReturnBooking(Long id, UpdateBookingReq updateBooking) {
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        existingBooking.setStatus(updateBooking.getStatus());
        existingBooking.setReturnAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy")));
//        existingBooking.setReturnAt(updateBooking.getReturnAt());
        Booking updatedBooking = bookingRepository.save(existingBooking);
        return convertToDTO(updatedBooking);
    }
    public BookingDto updateCancelBooking(Long id, UpdateBookingReq updateBooking) {
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        existingBooking.setStatus(updateBooking.getStatus());
        existingBooking.setCancelAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy")));
//        existingBooking.setReturnAt(updateBooking.getReturnAt());
        Booking updatedBooking = bookingRepository.save(existingBooking);
        return convertToDTO(updatedBooking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private BookingDto convertToDTO(Booking booking) {
        BookingDto bookingDTO = new BookingDto();
        bookingDTO.setIdBooking(booking.getId());
        bookingDTO.setTypeRoom(booking.getRoom().getTypeRoom());
        bookingDTO.setIdRoom(booking.getRoom().getId());
        bookingDTO.setFullName(booking.getUser().getFullName());
        bookingDTO.setCheckIn(String.valueOf(booking.getCheckIn()));
        bookingDTO.setCheckOut(String.valueOf(booking.getCheckOut()));
        bookingDTO.setPrice(String.valueOf(booking.getPrice()));
        bookingDTO.setPaymentStatus(booking.getPaymentStatus());
        bookingDTO.setStatus(String.valueOf(booking.getStatus()));
        bookingDTO.setRoomNumber(String.valueOf(booking.getRoom().getRoomNumber()));
        bookingDTO.setBookingAt(booking.getBookingAt());
        bookingDTO.setConfirmAt(booking.getConfirmAt());
        bookingDTO.setReturnAt(booking.getReturnAt());
        bookingDTO.setCancelAt(booking.getCancelAt());
        return bookingDTO;
    }

    public double calculateMonthlyRevenue(int year, int month) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy");
        List<Booking> bookings = bookingRepository.findAll(); // Lấy tất cả các booking

        double revenue = bookings.stream()
                .filter(booking -> {
                    LocalDate bookingDate = LocalDate.parse(booking.getBookingAt(), formatter);
                    return bookingDate.getYear() == year
                            && bookingDate.getMonthValue() == month
                            && "Returned".equals(booking.getStatus());
                })
                .mapToDouble(Booking::getPrice)
                .sum();

        return revenue;
    }
    public Optional<Booking> getBookingById(long idBooking) {
        return bookingRepository.findById(idBooking);
    }
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<BookingDto> getBookingByIdUser(Long idUser) {
         return bookingRepository.findByUserId(idUser).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public List<BookingDto> getRecentBookings() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy");
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .sorted((b1, b2) -> {
                    LocalDateTime date1 = LocalDateTime.parse(b1.getBookingAt(), formatter);
                    LocalDateTime date2 = LocalDateTime.parse(b2.getBookingAt(), formatter);
                    return date2.compareTo(date1); // Sắp xếp giảm dần
                })
                .limit(5)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public RevenueComparison getCurrentMonthRevenue() {
        LocalDate now = LocalDate.now();

        // Doanh thu tháng hiện tại
        double currentMonthRevenue = calculateMonthlyRevenue(now.getYear(), now.getMonthValue());

        RevenueComparison revenueComparison = new RevenueComparison();
        revenueComparison.setCurrentMonthRevenue(currentMonthRevenue);

        return revenueComparison;
    }

}
