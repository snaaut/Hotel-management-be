package com.datn.hotelmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.datn.hotelmanagement.Repository.*;
import com.datn.hotelmanagement.dto.request.EditUserReq;
import com.datn.hotelmanagement.dto.response.UserResponse;
import com.datn.hotelmanagement.entity.*;
import com.datn.hotelmanagement.mapper.AccountMapper;
import com.datn.hotelmanagement.mapper.UserMapper;
import com.datn.hotelmanagement.utils.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;


@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookedRoomRepository bookedRoomRepository;

    public void createUser(Account inputUser) {
        // Encode input password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        inputUser.setPassword(passwordEncoder.encode(inputUser.getPassword())) ;
        accountRepo.save(inputUser);
    }

    public Optional<Account> FindById(Long id) {
        return accountRepo.findById(id);
    }

    public List<Account> FindAll() {
        return accountRepo.findAll();
    }

    public Boolean existsByUserName(String userName) {
        return accountRepo.existsByUserName(userName);
    }

    public Boolean existsByEmail(String email) {
        return accountRepo.existsByEmail(email);
    }

    public Account findByUserName(String userName) {
        return accountRepo.findByUserName(userName).orElse(null);
    }
    public Account findById(Long id) {
        return accountRepo.findById(id).orElse(null);
    }
    public Optional<Account> getAccountById(Long id) {
        return accountRepo.findById(id);
    }
    public Boolean existsByPhoneNumber(String phoneNumber) {
        return accountRepo.existsByPhoneNumber(phoneNumber);
    }

    public List<Account> getUserList(Account user) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatcher::ignoreCase);

        Example<Account> example = Example.of(user, matcher);
        return accountRepo.findAll(example);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account user = accountRepo.findByUserName(name).orElse(null);
        return userMapper.toUserResponse(user);
    }
    public long getUserCount() {
        return accountRepo.countUsers();
    }

    public UserResponse updateUser(Account user, EditUserReq editUserReq) {
        userMapper.updateUser(user, editUserReq);
        return userMapper.toUserResponse(accountRepo.save(user));
    }

    public List<Account> selectAccountList(Account account) {
        return accountMapper.selectAccountList(account);
    }
    @Transactional
    public void deleteAccountById(Long accountId) {
        // Xóa tất cả các booking liên quan đến user
        List<Booking> bookings = bookingRepository.findByUserId(accountId);
        for (Booking booking : bookings) {
            // Xóa tất cả các booked room liên quan đến booking
            List<BookedRoom> bookedRooms = bookedRoomRepository.findByIdBooking(booking.getId());
            bookedRoomRepository.deleteAll(bookedRooms);
        }
        bookingRepository.deleteAll(bookings);

        // Xóa tất cả các review liên quan đến user
        List<Review> reviews = reviewRepository.findByUserId(accountId);
        reviewRepository.deleteAll(reviews);

        // Xóa account
        accountRepo.deleteById(accountId);
    }
}


