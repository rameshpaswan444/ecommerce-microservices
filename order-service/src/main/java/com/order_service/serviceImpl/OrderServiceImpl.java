package com.order_service.serviceImpl;

import com.order_service.client.InventoryClient;
import com.order_service.client.ProductClient;
import com.order_service.dto.request.CreateOrderRequest;
import com.order_service.dto.request.OrderItemRequest;
import com.order_service.dto.request.ReserveInventoryRequest;
import com.order_service.dto.response.InventoryResponse;
import com.order_service.dto.response.OrderItemResponse;
import com.order_service.dto.response.OrderResponse;
import com.order_service.dto.response.ProductResponse;
import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;
import com.order_service.enums.OrderStatus;
import com.order_service.exception.InsufficientStockException;
import com.order_service.repository.OrderItemRepository;
import com.order_service.repository.OrderRepository;
import com.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = Order.builder()
                .userId(request.getUserId())
                .orderNumber(generateOrderNumber())
                .totalAmount(BigDecimal.ZERO)
                .status(OrderStatus.PENDING)
                .shippingAddress(request.getShippingAddress())
                .build();

        order = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            // 1. Get product information
            ProductResponse product =
                    productClient.getProductById(
                            itemRequest.getProductId()
                    );

            // 2. Get inventory information
            InventoryResponse inventory =
                    inventoryClient.getInventoryByProductId(
                            itemRequest.getProductId()
                    );

            // 3. Check stock
            if (inventory.getAvailableQuantity()
                    < itemRequest.getQuantity()) {

                throw new InsufficientStockException(
                        "Insufficient stock for product: "
                                + product.getName()
                                + ". Available: "
                                + inventory.getAvailableQuantity()
                                + ", Requested: "
                                + itemRequest.getQuantity()
                );
            }

            inventoryClient.reserveInventory(
                    itemRequest.getProductId(),
                    ReserveInventoryRequest.builder()
                            .quantity(itemRequest.getQuantity())
                            .build()
            );

            // 4. Calculate subtotal
            BigDecimal unitPrice = product.getPrice();

            BigDecimal subtotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );

            // 5. Create order item
            OrderItem item = OrderItem.builder()
                    .orderId(order.getId())
                    .productId(product.getId())
                    .productName(product.getName())
                    .sku(product.getSku())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            orderItemRepository.save(item);

            // 6. Add to total
            totalAmount = totalAmount.add(subtotal);
        }

        // 7. Set final order total
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, String status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        order.setStatus(
                OrderStatus.valueOf(status.toUpperCase())
        );

        return mapToResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with id: " + id));

        // Only PENDING orders can be cancelled
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Order cannot be cancelled because its status is "
                            + order.getStatus()
            );
        }

        // Find all items belonging to this order
        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        // Release reserved inventory
        for (OrderItem item : items) {

            inventoryClient.releaseInventory(
                    item.getProductId(),
                    ReserveInventoryRequest.builder()
                            .quantity(item.getQuantity())
                            .build()
            );
        }

        // Change order status
        order.setStatus(OrderStatus.CANCELLED);

        return mapToResponse(
                orderRepository.save(order)
        );
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id));

        orderItemRepository.deleteByOrderId(order.getId());

        orderRepository.delete(order);
    }

    private String generateOrderNumber() {

        return "ORD-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private OrderResponse mapToResponse(Order order) {

        List<OrderItemResponse> items =
                orderItemRepository.findByOrderId(order.getId())
                        .stream()
                        .map(item -> OrderItemResponse.builder()
                                .id(item.getId())
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .sku(item.getSku())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subtotal(item.getSubtotal())
                                .build())
                        .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .items(items)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}