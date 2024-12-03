package com.datn.hotelmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.datn.hotelmanagement.Repository.AccountRepo;
import com.datn.hotelmanagement.common.domain.ApiResponse;
import com.datn.hotelmanagement.common.domain.Constants;
import com.datn.hotelmanagement.dto.request.EditUserReq;
import com.datn.hotelmanagement.dto.response.UserResponse;
import com.datn.hotelmanagement.entity.Account;
import com.datn.hotelmanagement.service.AccountService;
import com.datn.hotelmanagement.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("user")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepo accountRepo;


    @GetMapping("/my-info")
    public ApiResponse getMyInfo() {
        UserResponse userResponse = accountService.getMyInfo();
        ApiResponse response = ApiResponse.success();
        response.put("Info", userResponse);
        return response;
    }
    @GetMapping("/get-user/{id}")
    public ApiResponse getUserById(@PathVariable Long id) {
        Optional<Account> optionalUser = accountRepo.findById(id);
        if (optionalUser.isPresent()) {
            Account user = optionalUser.get();
            return ApiResponse.success("User found", user);
        } else {
            return ApiResponse.error("User not found");
        }
    }

    @PostMapping("/my-info/edit")
    @ResponseBody
    public ApiResponse editMyInfo(@RequestBody EditUserReq info) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account user = accountService.findByUserName(name);
        if (user == null) {
            return ApiResponse.error("Không tìm thấy thông tin người dùng");
        }
        /** Validate input*/
        if (StringUtils.isNotEmpty(info.getEmail()) && !Pattern.compile(Constants.EMAIL_REGEX).matcher(info.getEmail()).matches()) {
            return ApiResponse.error("Email không đúng định dạng, vui lòng thử lại");
        }
        if (StringUtils.isNotEmpty(info.getPhoneNumber()) && !Pattern.compile(Constants.PHONE_REGEX).matcher(info.getPhoneNumber()).matches()) {
            return ApiResponse.error("Số điện thoại không đúng định dạng, vui lòng thử lại");
        }
//        if (StringUtils.isNotEmpty(info.getEmail()) && !info.getEmail().equals(user.getEmail()) && userService.existsByEmail(info.getEmail())) {
//            return ApiResponse.error("Email đã được sử dụng trong hệ thống, vui lòng thử lại");
//        }
//        if (StringUtils.isNotEmpty(info.getPhoneNumber()) && !info.getPhoneNumber().equals(user.getPhoneNumber()) ) {
//            return ApiResponse.error("Số điện thoại đã được sử dụng trong hệ thống, vui lòng thử lại");
//        }
        ApiResponse response = ApiResponse.success("Update thành công");
        UserResponse userResponse = accountService.updateUser(user, info);
        response.put("info", userResponse);
        return response;
    }
    @PostMapping("/update-user/{id}")
    @ResponseBody
    public ApiResponse UpdateInfo(@PathVariable Long id, @RequestBody EditUserReq info) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account user = accountService.findById(id);
        if (user == null) {
            return ApiResponse.error("Không tìm thấy thông tin người dùng");
        }
        /** Validate input*/
        if (StringUtils.isNotEmpty(info.getEmail()) && !Pattern.compile(Constants.EMAIL_REGEX).matcher(info.getEmail()).matches()) {
            return ApiResponse.error("Email không đúng định dạng, vui lòng thử lại");
        }
        if (StringUtils.isNotEmpty(info.getPhoneNumber()) && !Pattern.compile(Constants.PHONE_REGEX).matcher(info.getPhoneNumber()).matches()) {
            return ApiResponse.error("Số điện thoại không đúng định dạng, vui lòng thử lại");
        }

        ApiResponse response = ApiResponse.success("Update thành công");
        UserResponse userResponse = accountService.updateUser(user, info);
        response.put("info", userResponse);
        return response;
    }





    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/getAll")
    @ResponseBody
    public ApiResponse getAllUser() {
        ApiResponse response = ApiResponse.success();
        List<Account> accounts = accountRepo.findAll();
        response.put("users", accounts);
        return response;

    }

    @GetMapping("/search")
    @ResponseBody
    public ApiResponse searchUsers(@RequestParam String keyword) {
        List<Account> users = accountRepo.searchByUsernameOrPhoneNumber(keyword);
        if (users.isEmpty()) {
            return ApiResponse.error("No users found");
        }
        ApiResponse response = ApiResponse.success("Users found");
        response.put("users", users);
        return response;
    }
    @DeleteMapping("/delete-user/{id}")
    public ApiResponse deleteUserById(@PathVariable Long id) {
        Optional<Account> optionalUser = accountRepo.findById(id);
        if (optionalUser.isPresent()) {
            accountService.deleteAccountById(id);
            return ApiResponse.success("User deleted successfully");
        } else {
            return ApiResponse.error("User not found");
        }
    }
    @GetMapping("/count")
    public ApiResponse getUserCount() {
        long userCount = accountService.getUserCount();
        ApiResponse response = ApiResponse.success();
        response.put("userCount", userCount);
        return response;
    }
}