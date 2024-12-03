package com.datn.hotelmanagement.service;

import com.datn.hotelmanagement.Repository.PaymentRepository;
import com.datn.hotelmanagement.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Optional<Payment> getPaymentByBookingId(Long idBooking) {
        return paymentRepository.findByIdBooking(idBooking);
    }
}