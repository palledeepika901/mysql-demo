package com.example.orders.domain;

import java.time.Instant;
import java.util.Objects;

public record OrderCancelled(OrderId orderId, String reason, Instant occurredAt) {
    public OrderCancelled {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(occurredAt, "Timestamp cannot be null");
    }
}