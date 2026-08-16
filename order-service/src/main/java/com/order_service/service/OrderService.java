package com.order_service.service;

import com.order_service.dto.request.CreateOrderRequest;
import com.order_service.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse confirmPayment(Long id);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(Long id, String status);

    OrderResponse cancelOrder(Long id);

    void deleteOrder(Long id);
}
