package com.order_service.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private String paymentMethod;
}