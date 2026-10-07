package com.example.orders.adapters;

import com.example.orders.application.NotificationPort;
import com.example.orders.domain.OrderId;

public class EmailNotificationAdapter implements NotificationPort {
    @Override
    public void sendOrderApproved(OrderId orderId) {
        System.out.println("[EMAIL OUTBOX] Order " + orderId.value() + " has been approved.");
    }
}