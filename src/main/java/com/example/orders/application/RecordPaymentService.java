
package com.example.orders.application;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.example.orders.domain.Clock;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderId;
import com.example.orders.domain.OrderRepository;
import com.example.orders.domain.PaymentRecorded;

public class RecordPaymentService {

    private final OrderRepository repository;
    private final Clock clock;

    public RecordPaymentService(OrderRepository repository, Clock clock) {
        this.repository = Objects.requireNonNull(repository, "repository cannot be null");
        this.clock = Objects.requireNonNull(clock, "clock cannot be null");
    }

    public PaymentRecorded recordPayment(OrderId id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id.value()));

        order.recordPayment();
        repository.save(order);

        return new PaymentRecorded(order.getId(), order.total(), clock.now());
    }
}