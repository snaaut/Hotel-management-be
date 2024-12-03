package com.datn.hotelmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReqDto {
    private String userName;
    private String password;
    private String confirmPassword;
    private String email;
}
