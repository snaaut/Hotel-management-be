package com.datn.hotelmanagement.controller;

import com.datn.hotelmanagement.entity.Review;
import com.datn.hotelmanagement.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("get-all")
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("get/{id}")
    public List<Review> getReviewByRoomId(@PathVariable long id) {
        return reviewService.getReviewByRoomId(id);
    }

    @PostMapping("create-review")
    public Review createReview(@RequestBody Review review) {
        review.setReviewAt(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy"))));
        return reviewService.saveReview(review);
    }

    @DeleteMapping("delete/{id}")
    public void deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
    }
    @GetMapping("/check-reviewed")
    public boolean checkUserReviewedRoom(@RequestParam Long userId, @RequestParam Long roomId) {
        return reviewService.checkUserReviewedRoom(userId, roomId);
    }
}
