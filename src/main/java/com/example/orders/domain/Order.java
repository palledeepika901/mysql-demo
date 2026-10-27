package com.example.orders.domain; // Ensure this matches your project structure

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    
    private OrderId id; //[span_0](start_span)[span_0](end_span)
    private final List<OrderLine> lines = new ArrayList<>(); //[span_1](start_span)[span_1](end_span)
    private OrderStatus status; //[span_2](start_span)[span_2](end_span)

    // New fields added for Step 2
    private String productName; //[span_3](start_span)[span_3](end_span)
    private int quantity; //[span_4](start_span)[span_4](end_span)
    private double price; //[span_5](start_span)[span_5](end_span)

    // Updated constructor to accept the new fields
    public Order(OrderId id, String productName, int quantity, double price) { //[span_6](start_span)[span_6](end_span)
        this.id = Objects.requireNonNull(id, "Order ID cannot be null"); //[span_7](start_span)[span_7](end_span)
        this.status = OrderStatus.DRAFT; //[span_8](start_span)[span_8](end_span)
        
        this.productName = productName; //[span_9](start_span)[span_9](end_span)
        this.quantity = quantity; //[span_10](start_span)[span_10](end_span)
        this.price = price; //[span_11](start_span)[span_11](end_span)
    }

    // Restored original constructor for the test suite
    public Order(OrderId id) { //[span_12](start_span)[span_12](end_span)
        this.id = Objects.requireNonNull(id, "Order ID cannot be null"); //[span_13](start_span)[span_13](end_span)
        this.status = OrderStatus.DRAFT; //[span_14](start_span)[span_14](end_span)
    }

    // Default constructor
    public Order() { 
    }

    // Setter
    public void setStatus(OrderStatus status) { //[span_15](start_span)[span_15](end_span)
        this.status = status; //[span_16](start_span)[span_16](end_span)
    }

    public void addLine(OrderLine line) { //[span_17](start_span)[span_17](end_span)
        if (this.status != OrderStatus.DRAFT) { //[span_18](start_span)[span_18](end_span)
            throw new IllegalStateException("Cannot add items to an order in " + this.status + " status"); //[span_19](start_span)[span_19](end_span)
        }
        this.lines.add(Objects.requireNonNull(line, "Order line cannot be null")); //[span_20](start_span)[span_20](end_span)
    }

    public void confirm() { //[span_21](start_span)[span_21](end_span)
        if (this.status != OrderStatus.DRAFT) { //[span_22](start_span)[span_22](end_span)
            throw new IllegalStateException("Only draft orders can be confirmed. Current status: " + this.status); //[span_23](start_span)[span_23](end_span)
        }
        if (this.lines.isEmpty()) { //[span_24](start_span)[span_24](end_span)
            throw new IllegalStateException("An order must contain at least one line before confirmation."); //[span_25](start_span)[span_25](end_span)
        }
        this.status = OrderStatus.CONFIRMED; //[span_26](start_span)[span_26](end_span)
    }

    // Business rule: Cancel an order
    public void cancel(String reason) { //[span_27](start_span)[span_27](end_span)
        if (this.status == OrderStatus.PAID) { //[span_28](start_span)[span_28](end_span)
            throw new IllegalStateException("paid order cannot return to draft or be cancelled"); //[span_29](start_span)[span_29](end_span)
        }
        this.status = OrderStatus.CANCELLED; //[span_30](start_span)[span_30](end_span)
    }

    // Business rule: Record a payment
    public void recordPayment() { //[span_31](start_span)[span_31](end_span)
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("cancelled order cannot be paid");
        }
        this.status = OrderStatus.PAID; 
    }

    // Business rule: Calculate total
    public double total() {
        return this.price * this.quantity; 
    }

    // Appropriate Getters and Setters generated
    public String getProductName() { //[span_32](start_span)[span_32](end_span)
        return productName; //[span_33](start_span)[span_33](end_span)
    }

    public void setProductName(String productName) { //[span_34](start_span)[span_34](end_span)
        this.productName = productName; //[span_35](start_span)[span_35](end_span)
    }

    public int getQuantity() { //[span_36](start_span)[span_36](end_span)
        return quantity; //[span_37](start_span)[span_37](end_span)
    }

    public void setQuantity(int quantity) { //[span_38](start_span)[span_38](end_span)
        this.quantity = quantity; //[span_39](start_span)[span_39](end_span)
    }

    public double getPrice() { //[span_40](start_span)[span_40](end_span)
        return price; //[span_41](start_span)[span_41](end_span)
    }

    public void setPrice(double price) { //[span_42](start_span)[span_42](end_span)
        this.price = price; //[span_43](start_span)[span_43](end_span)
    }

    public OrderId getId() { //[span_44](start_span)[span_44](end_span)
        return id; //[span_45](start_span)[span_45](end_span)
    }

    public OrderStatus getStatus() { //[span_46](start_span)[span_46](end_span)
        return status; //[span_47](start_span)[span_47](end_span)
    }
}