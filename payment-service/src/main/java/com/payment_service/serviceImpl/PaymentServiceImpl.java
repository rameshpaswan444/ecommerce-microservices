package com.payment_service.serviceImpl;

import com.payment_service.client.OrderClient;
import com.payment_service.dto.request.CreatePaymentRequest;
import com.payment_service.dto.response.PaymentResponse;
import com.payment_service.entity.Payment;
import com.payment_service.enums.PaymentStatus;
import com.payment_service.mapper.PaymentMapper;
import com.payment_service.repository.PaymentRepository;
import com.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;

    @Value("${internal.service.secret}")
    private String internalServiceSecret;

    @Override
    public PaymentResponse createPayment(
            CreatePaymentRequest request) {

        if (paymentRepository.existsByOrderId(
                request.getOrderId())) {

            throw new IllegalStateException(
                    "Payment already exists for order: "
                            + request.getOrderId());
        }

        Payment payment =
                paymentMapper.toEntity(request);

        payment.setPaymentNumber(
                generatePaymentNumber());

        payment.setStatus(PaymentStatus.PENDING);

        payment.setTransactionId(null);

        payment =
                paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with id: "
                                                + id));

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            Long orderId) {

        Payment payment =
                paymentRepository.findByOrderId(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found for order: "
                                                + orderId));

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUserId(
            Long userId) {

        return paymentRepository
                .findByUserId(userId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    public PaymentResponse processPayment(Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with id: "
                                                + id));

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new IllegalStateException(
                    "Payment cannot be processed from status: "
                            + payment.getStatus());
        }

        payment.setStatus(
                PaymentStatus.SUCCESS);

        payment.setTransactionId(
                generateTransactionId());

        payment =
                paymentRepository.save(payment);

        orderClient.confirmPayment(
                payment.getOrderId(),
                internalServiceSecret
        );

        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse failPayment(Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with id: "
                                                + id));

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new IllegalStateException(
                    "Payment cannot be failed from status: "
                            + payment.getStatus());
        }

        payment.setStatus(
                PaymentStatus.FAILED);

        payment.setTransactionId(null);

        return paymentMapper.toResponse(
                paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse refundPayment(Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with id: "
                                                + id));

        if (payment.getStatus()
                != PaymentStatus.SUCCESS) {

            throw new IllegalStateException(
                    "Only successful payments can be refunded");
        }

        payment.setStatus(
                PaymentStatus.REFUNDED);

        return paymentMapper.toResponse(
                paymentRepository.save(payment));
    }

    private String generatePaymentNumber() {

        return "PAY-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private String generateTransactionId() {

        return "TXN-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 12)
                .toUpperCase();
    }
}