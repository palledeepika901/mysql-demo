package com.example.orders.domain;

import java.time.LocalDate;
import java.util.List;

public class StandardOrder extends BaseOrder {

    private final double shippingFee;

    // Full 5-parameter constructor
    public StandardOrder(String orderId, LocalDate createdAt, OrderStatus status, List<OrderItem> items, double shippingFee) {
        super(orderId, createdAt, status, items);
        this.shippingFee = shippingFee;
    }

    // Overloaded 4-parameter constructor (defaults shippingFee to 0.0)
    public StandardOrder(String orderId, LocalDate createdAt, OrderStatus status, List<OrderItem> items) {
        this(orderId, createdAt, status, items, 0.0);
    }

    public double getShippingFee() {
        return shippingFee;
    }

    @Override
    public double calculateFinalTotal() {
        double subtotal = getItems().stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
        return subtotal + shippingFee;
    }
}