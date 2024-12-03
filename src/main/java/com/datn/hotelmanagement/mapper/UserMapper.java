package com.datn.hotelmanagement.mapper;


import com.datn.hotelmanagement.dto.request.EditUserReq;
import com.datn.hotelmanagement.dto.request.RegisterReqDto;
import com.datn.hotelmanagement.dto.response.UserResponse;
import com.datn.hotelmanagement.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(Account user);

    Account toUser(RegisterReqDto registerReqDto);

    void updateUser(@MappingTarget Account user, EditUserReq editUserReq);
}
