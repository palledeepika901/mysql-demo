package com.example.orders.application;

import com.example.orders.domain.OrderId;

public interface NotificationPort {
    void sendOrderApproved(OrderId orderId);
}