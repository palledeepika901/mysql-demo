package com.example.orders.domain;

public class OrderValidationException extends Exception {
    public OrderValidationException(String message) {
        super(message);
    }
}