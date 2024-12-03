package com.datn.hotelmanagement.mapper;

import com.datn.hotelmanagement.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AccountMapper {

    List<Account> selectAccountList(Account account);
}
