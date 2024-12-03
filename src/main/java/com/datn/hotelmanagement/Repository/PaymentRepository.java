package com.datn.hotelmanagement.Repository;

import com.datn.hotelmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByIdBooking(Long idBooking);
}
