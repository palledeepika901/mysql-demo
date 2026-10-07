package com.example.orders.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {

    private final OrderId id;
    private final List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status;

    public Order(OrderId id) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        this.status = OrderStatus.DRAFT;
    }

    public void addLine(OrderLine line) {
        if (this.status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot add items to an order in " + this.status + " status");
        }
        this.lines.add(Objects.requireNonNull(line, "Order line cannot be null"));
    }

    public void confirm() {
        if (this.status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Only draft orders can be confirmed. Current status: " + this.status);
        }
        if (this.lines.isEmpty()) {
            throw new IllegalStateException("An order must contain at least one line before confirmation.");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void recordPayment() {
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("A cancelled order cannot be paid.");
        }
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed orders can be paid. Current status: " + this.status);
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel(String reason) {
        if (this.status == OrderStatus.PAID) {
            throw new IllegalStateException("A paid order cannot return to draft or be cancelled.");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled.");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public Money total() {
        BigDecimal totalAmount = lines.stream()
                .map(line -> line.lineTotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Money(totalAmount);
    }

    public OrderId getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(lines);
    }
}

   