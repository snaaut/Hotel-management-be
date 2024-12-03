package com.datn.hotelmanagement.dto.response;

import com.datn.hotelmanagement.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long idBooking;
    private String typeRoom;
    private Long idRoom;
    private String roomNumber;
    private String fullName;
    private String checkIn;
    private String checkOut;
    private String price;
    private String bookingAt;
    private String confirmAt;
    private String returnAt;
    private String cancelAt;
    private String paymentStatus;
    private String status;

}
