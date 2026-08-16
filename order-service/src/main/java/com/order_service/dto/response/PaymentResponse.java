package com.order_service.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;

    private String paymentNumber;

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private String paymentMethod;

    private String status;

    private String transactionId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}