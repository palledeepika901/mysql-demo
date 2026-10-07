package com.example.orders.domain;

import java.time.Instant;
import java.util.Objects;

public record OrderConfirmed(OrderId orderId, Instant occurredAt) {
    public OrderConfirmed {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(occurredAt, "Timestamp cannot be null");
    }
}