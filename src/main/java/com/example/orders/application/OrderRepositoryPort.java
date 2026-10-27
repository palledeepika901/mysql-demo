package com.example.orders.application;

import com.example.orders.domain.Order;

public interface OrderRepositoryPort {
    void save(Order order);
}