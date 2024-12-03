package com.datn.hotelmanagement.Repository;

import com.datn.hotelmanagement.entity.InvalidatedToken;
import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String>{
}
