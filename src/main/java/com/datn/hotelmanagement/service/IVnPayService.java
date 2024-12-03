package com.datn.hotelmanagement.service;

import jakarta.servlet.http.HttpServletRequest;

public interface IVnPayService {
    String createPaymentUrl(HttpServletRequest request, Long amount, String orderInfo);

    Long bookingReturn(HttpServletRequest request);
}
