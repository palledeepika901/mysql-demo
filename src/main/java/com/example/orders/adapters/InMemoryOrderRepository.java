package com.example.orders.adapters;

import com.example.orders.application.OrderRepositoryPort;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepositoryPort {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(store.get(id.value()));
    }

    @Override
    public void save(Order order) {
        store.put(order.getId().value(), order);
    }
}