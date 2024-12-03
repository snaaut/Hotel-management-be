package com.datn.hotelmanagement.controller;

import com.datn.hotelmanagement.Repository.PaymentRepository;
import com.datn.hotelmanagement.common.domain.ApiResponse;
import com.datn.hotelmanagement.entity.Account;
import com.datn.hotelmanagement.entity.Booking;
import com.datn.hotelmanagement.entity.Payment;
import com.datn.hotelmanagement.service.AccountService;
import com.datn.hotelmanagement.service.BookingService;
import com.datn.hotelmanagement.service.IVnPayService;
import com.datn.hotelmanagement.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    IVnPayService vnpayService;

    @Autowired
    BookingService bookingService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ApiResponse CreatePayment(@RequestParam("amount") Long amount, @RequestParam("idBooking") Long idBooking,
                                     HttpServletRequest request) {
        // Validate
        Optional<Booking> booking = bookingService.getBookingById(idBooking);

        if (booking.isEmpty()) {
            return ApiResponse.error("Booking not found");
        }
//        if (booking.get().getPaymentStatus().equals("Y")) {
//            return ApiResponse.error("Order đã được thanh toán");
//        }
        ApiResponse response = ApiResponse.success();
        String vnpayUrl = vnpayService.createPaymentUrl(request, amount, idBooking.toString());
        response.put("vnpayUrl", vnpayUrl);
        //System.out.println(vnpayUrl);
        return response;
    }

    @GetMapping("/get-details/{idBooking}")
    public ResponseEntity<Payment> getPaymentDetails(@PathVariable Long idBooking) {
        Optional<Payment> payment = paymentService.getPaymentByBookingId(idBooking);
        if (payment.isPresent()) {
            return ResponseEntity.ok(payment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/return")
    public RedirectView PaymentReturn(HttpServletRequest request) {
        System.out.println("ok");
        String successUrl = "http://localhost:3000/payment/success";
        String errorUrl = "http://localhost:3000/payment/error";
        Long paymentStatus = vnpayService.bookingReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String totalAmount = request.getParameter("vnp_Amount");

        Long idBooking = Long.parseLong(orderInfo);
        Optional<Booking> booking = bookingService.getBookingById(idBooking);

        Account user = accountService.findById(booking.get().getUser().getId());

        RedirectView redirectView = new RedirectView();
        redirectView.addStaticAttribute("paymentTime", paymentTime);
        redirectView.addStaticAttribute("totalAmount", totalAmount);
        redirectView.addStaticAttribute("transactionNo", transactionNo);
        redirectView.addStaticAttribute("idUser", booking.get().getUser().getId());
        redirectView.addStaticAttribute("address", booking.get().getUser().getAddress());
        redirectView.addStaticAttribute("city", booking.get().getUser().getCity());
        redirectView.addStaticAttribute("userFullName", user.getFullName());
        redirectView.addStaticAttribute("country", booking.get().getUser().getCountry());

        if (paymentStatus == 1) {
//            booking.get().setStatus("paid");
            booking.get().setPaymentStatus("Paid");
            bookingService.getBookingById(idBooking);
            Optional<Booking> bookingOpt = bookingService.getBookingById(idBooking);

            if (bookingOpt.isPresent()) {
                Booking booking1 = bookingOpt.get();
                Account user1 = accountService.findById(booking1.getUser().getId());

                Payment payment = new Payment();
                payment.setPayAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd/MM/yyyy")));
                payment.setTotalAmount(Double.parseDouble((totalAmount))/100);
                payment.setIdBooking(booking1.getId());
                payment.setTransactionNo(transactionNo);
                payment.setPaymentStatus("Paid");
                payment.setAddress(booking1.getUser().getAddress());
                payment.setCity(booking1.getUser().getCity());
                payment.setUserFullName(user1.getFullName());
                payment.setCountry(booking1.getUser().getCountry());

                paymentRepository.save(payment);
            }
                redirectView.setUrl(successUrl);
                return redirectView;
            } else {
                //booking.get().setStatus("error");
                //orderService.updateOrder(booking);
                redirectView.setUrl(errorUrl);
                return redirectView;
            }
        }
    }

