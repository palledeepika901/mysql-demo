package com.example.orders.application;

import com.example.orders.domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class OrderService {
    private final List<BaseOrder> orders = new ArrayList<>();

    public void addOrder(BaseOrder order) throws OrderValidationException {
        if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderValidationException("Order must contain at least one item.");
        }
        orders.add(order);
    }

    public BaseOrder findById(String orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId().equalsIgnoreCase(orderId))
                .findFirst()
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found."));
    }

    // Filter using Java Streams & Lambdas
    public List<BaseOrder> filterByStatus(OrderStatus status) {
        return orders.stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    // Sort by total order value descending
    public List<BaseOrder> getOrdersSortedByTotal() {
        return orders.stream()
                .sorted(Comparator.comparingDouble(BaseOrder::calculateFinalTotal).reversed())
                .collect(Collectors.toList());
    }
}