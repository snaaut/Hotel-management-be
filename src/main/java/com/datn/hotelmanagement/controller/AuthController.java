package com.datn.hotelmanagement.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import com.datn.hotelmanagement.common.domain.ApiResponse;
import com.datn.hotelmanagement.dto.request.RegisterReqDto;
import com.datn.hotelmanagement.entity.Account;
import com.datn.hotelmanagement.entity.InvalidatedToken;
import com.datn.hotelmanagement.enums.Role;
import com.datn.hotelmanagement.mapper.UserMapper;
import com.datn.hotelmanagement.service.AccountService;
import com.datn.hotelmanagement.service.AuthenticationService;
import com.datn.hotelmanagement.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AccountService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    @ResponseBody
    public ApiResponse registerUser(@RequestBody RegisterReqDto registerInfo) {
        if (StringUtils.isEmpty(registerInfo.getUserName().trim())) {
            return ApiResponse.error("Username không được để trống");
        }
        if (StringUtils.isEmpty(registerInfo.getPassword().trim())
                && StringUtils.isEmpty(registerInfo.getConfirmPassword())) {
            return ApiResponse.error("Password không được để trống.");
        }
        if (!registerInfo.getPassword().equals(registerInfo.getConfirmPassword())) {
            return ApiResponse.error("Mật khẩu xác nhận không khớp.");
        }
        if (userService.existsByUserName(registerInfo.getUserName())) {
            return ApiResponse.error("Username đã tồn tại.");
        }
        if (registerInfo.getEmail() != null && userService.existsByEmail(registerInfo.getEmail())) {
            return ApiResponse.error("Email đã được sử dụng.");
        }
        Account user = userMapper.toUser(registerInfo);
        user.setRole(Role.USER.name());
        userService.createUser(user);
        ApiResponse apiResponse = ApiResponse.success("Đăng ký thành công");
        apiResponse.put("user", user);

        String token = authenticationService.generateToken(user);
        apiResponse.put("token", token);

        return apiResponse;
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@RequestBody Account userLogin) {
        if (StringUtils.isEmpty(userLogin.getUserName().trim())) {
            return ApiResponse.error("Username không được để trống");
        }
        if (StringUtils.isEmpty(userLogin.getPassword().trim())) {
            return ApiResponse.error("Password không được để trống.");
        }
        Account user = userService.findByUserName(userLogin.getUserName());
        if (user == null) {
            return ApiResponse.error("User not found");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            return ApiResponse.error("Mật khẩu không chính xác.");
        }
        String token = authenticationService.generateToken(user);
        ApiResponse response = ApiResponse.success();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("idUser", user.getId());
        return response;
    }

    @GetMapping("/logout")
    @ResponseBody
    public ApiResponse logout(HttpServletRequest request) throws ParseException, JOSEException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return ApiResponse.warn("Nothing to logout!");
        }
        if (!authenticationService.verifyToken(token.substring(7))) {
            return ApiResponse.error("Token is invalid");
        }
        SignedJWT signedJWT = SignedJWT.parse(token.substring(7));

        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpireTime(expiryTime);
        authenticationService.saveInvalidatedToken(invalidatedToken);

        return ApiResponse.success("Đăng xuất thành công!");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public ApiResponse getAll(@RequestBody Account account) {
        return ApiResponse.success(accountService.selectAccountList(account));
    }
    @GetMapping("/role")
    public ResponseEntity<String> getRoleFromToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix if it exists
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String role = authenticationService.getRoleFromToken(token);
            return ResponseEntity.ok(role);
        } catch (ParseException | JOSEException e) {
            return ResponseEntity.status(400).body("Invalid token");
        }
    }
}
