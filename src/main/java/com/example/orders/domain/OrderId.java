package com.example.orders.domain;

import java.util.Objects;
import java.util.UUID;

public record OrderId(String value) {
    public OrderId {
        Objects.requireNonNull(value, "Order ID cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be blank");
        }
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID().toString());
    }
}