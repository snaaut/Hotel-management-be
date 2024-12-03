package com.datn.hotelmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idRoom")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "idUser")
    private Account user;
    private Date checkIn;
    private Date checkOut;
    private double price;
    private String status;
    private String paymentStatus;
    private String bookingAt;
    private String confirmAt;
    private String cancelAt;
    private String returnAt;

}
