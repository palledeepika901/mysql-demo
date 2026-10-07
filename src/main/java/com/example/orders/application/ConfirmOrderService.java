package com.example.orders.application;

import com.example.orders.domain.Clock;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderConfirmed;
import com.example.orders.domain.OrderId;
import com.example.orders.domain.OrderRepository;

import java.util.NoSuchElementException;

public class ConfirmOrderService {

    private final OrderRepository repository;
    private final Clock clock;

    public ConfirmOrderService(OrderRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public OrderConfirmed confirm(OrderId id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id.value()));

        order.confirm();
        repository.save(order);

        return new OrderConfirmed(order.getId(), clock.now());
    }
}