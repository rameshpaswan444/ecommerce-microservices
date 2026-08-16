package com.payment_service.service;

import com.payment_service.dto.request.CreatePaymentRequest;
import com.payment_service.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByOrderId(Long orderId);

    List<PaymentResponse> getPaymentsByUserId(Long userId);

    PaymentResponse processPayment(Long id);

    PaymentResponse failPayment(Long id);

    PaymentResponse refundPayment(Long id);
}