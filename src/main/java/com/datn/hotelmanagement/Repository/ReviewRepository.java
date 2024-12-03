package com.datn.hotelmanagement.Repository;

import com.datn.hotelmanagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.room.id = :roomId")
    List<Review> findByRoomId(@Param("roomId") Long roomId);
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.user.id = :userId AND r.room.id = :roomId")
    boolean existsByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
    List<Review> findByUserId(Long userId);
}
