package com.example.orders.domain;

import java.time.LocalDate;
import java.util.List;

public abstract class BaseOrder {
    private final String orderId;
    private final LocalDate createdAt;
    private OrderStatus status;
    private final List<OrderItem> items;

    public BaseOrder(String orderId, LocalDate createdAt, OrderStatus status, List<OrderItem> items) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.status = status;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public LocalDate getCreatedAt() { return createdAt; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<OrderItem> getItems() { return items; }

    // Abstract method forcing child classes to calculate totals with custom logic
    public abstract double calculateFinalTotal();
}