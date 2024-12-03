package com.datn.hotelmanagement.service;

import com.datn.hotelmanagement.Repository.ReviewRepository;
import com.datn.hotelmanagement.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewByRoomId(long id) {
        return reviewRepository.findByRoomId(id);
    }

    public Review saveReview(Review review) {

        return reviewRepository.save(review);
    }

    public void deleteReview(int id) {
        reviewRepository.deleteById(id);
    }
    public boolean checkUserReviewedRoom(Long userId, Long roomId) {
        return reviewRepository.existsByUserIdAndRoomId(userId, roomId);
    }
}
