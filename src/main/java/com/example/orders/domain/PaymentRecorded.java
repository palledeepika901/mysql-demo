

package com.example.orders.domain;

import java.time.Instant; // Change to LocalDateTime if your Clock uses that

public record PaymentRecorded(OrderId id, double total, Instant timestamp) {
}
