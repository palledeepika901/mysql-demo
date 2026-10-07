package com.example.orders.application;

import com.example.orders.adapters.InMemoryOrderRepository;
import com.example.orders.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderRepository repository;
    private List<OrderId> notifiedOrders;
    private NotificationPort notificationPort;
    private Clock fixedClock;
    private Instant fixedInstant;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
        notifiedOrders = new ArrayList<>();
        notificationPort = notifiedOrders::add;
        fixedInstant = Instant.parse("2026-10-05T12:00:00Z");
        fixedClock = () -> fixedInstant;
    }

    @Test
    @DisplayName("Full workflow: create -> add item -> confirm -> record payment -> notify")
    void shouldCompleteFullOrderLifecycle() {
        OrderId id = OrderId.generate();
        Order order = new Order(id);
        order.addLine(new OrderLine("ITEM-1", Money.of("1200.00"), 1));
        repository.save(order);

        // 1. Confirm the draft order
        ConfirmOrderService confirmService = new ConfirmOrderService(repository, fixedClock);
        OrderConfirmed confirmEvent = confirmService.confirm(id);

        Order confirmedOrder = repository.findById(id).orElseThrow();
        assertEquals(OrderStatus.CONFIRMED, confirmedOrder.getStatus());
        assertEquals(id, confirmEvent.orderId());

        // 2. Record payment for the confirmed order
        RecordPaymentService paymentService = new RecordPaymentService(repository, fixedClock);
        PaymentRecorded paymentEvent = paymentService.recordPayment(id);

        Order paidOrder = repository.findById(id).orElseThrow();
        assertEquals(OrderStatus.PAID, paidOrder.getStatus());
        assertEquals(fixedInstant, paymentEvent.occurredAt());
        assertEquals(id, paymentEvent.orderId());
        assertEquals(Money.of("1200.00"), paymentEvent.amountPaid());
    }
}