package com.order_service.client;

import com.order_service.config.FeignConfig;
import com.order_service.dto.request.CreatePaymentRequest;
import com.order_service.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PAYMENT-SERVICE",configuration = FeignConfig.class)
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponse createPayment(
            @RequestBody CreatePaymentRequest request
    );

    @GetMapping("/api/payments/order/{orderId}")
    PaymentResponse getPaymentByOrderId(
            @PathVariable Long orderId
    );


}