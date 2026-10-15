package com.example.orders.domain;

public record OrderItem(String itemId, String name, double price, int quantity) {
    public double getTotalPrice() {
        return price * quantity;
    }
}