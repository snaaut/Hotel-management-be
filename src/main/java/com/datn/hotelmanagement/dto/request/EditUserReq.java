package com.datn.hotelmanagement.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditUserReq {
    String email;
    String fullName;
    String phoneNumber;
    String address;
    String city;
    String country;
}
