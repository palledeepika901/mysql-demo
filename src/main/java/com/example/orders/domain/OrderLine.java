package com.example.orders.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record OrderLine(String productId, Money unitPrice, int quantity) {

    public OrderLine {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        Objects.requireNonNull(unitPrice, "Unit price cannot be null");

        if (productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive whole number");
        }
    }

    public Money lineTotal() {
        return new Money(unitPrice.amount().multiply(BigDecimal.valueOf(quantity)));
    }
}