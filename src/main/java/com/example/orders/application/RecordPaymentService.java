package com.example.orders.application;

import com.example.orders.domain.Clock;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderId;
import com.example.orders.domain.OrderRepository;
import com.example.orders.domain.PaymentRecorded;

import java.util.NoSuchElementException;

public class RecordPaymentService {

    private final OrderRepository repository;
    private final Clock clock;

    public RecordPaymentService(OrderRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public PaymentRecorded recordPayment(OrderId id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id.value()));

        order.recordPayment();
        repository.save(order);

        return new PaymentRecorded(order.getId(), order.total(), clock.now());
    }
}