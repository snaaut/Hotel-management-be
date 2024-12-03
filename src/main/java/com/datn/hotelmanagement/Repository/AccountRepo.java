package com.datn.hotelmanagement.Repository;

import com.datn.hotelmanagement.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<Account> findByUserName(String userName);

    @Query("SELECT a FROM Account a WHERE a.fullName LIKE %:keyword% OR a.phoneNumber LIKE %:keyword%")
    List<Account> searchByUsernameOrPhoneNumber(@Param("keyword") String keyword);
    @Query("SELECT COUNT(a) FROM Account a")
    long countUsers();
}
