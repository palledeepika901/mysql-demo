package com.example.orders.domain;

import java.time.Instant;
import java.util.Objects;

public record PaymentRecorded(OrderId orderId, Money amountPaid, Instant occurredAt) {
    public PaymentRecorded {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(amountPaid, "Amount paid cannot be null");
        Objects.requireNonNull(occurredAt, "Timestamp cannot be null");
    }
}
