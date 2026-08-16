package com.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {

    @PatchMapping("/api/orders/{id}/payment-confirm")
    void confirmPayment(
            @PathVariable("id") Long id,
            @RequestHeader("X-Internal-Service")
            String secret
    );
}