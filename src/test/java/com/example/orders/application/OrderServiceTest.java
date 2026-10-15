package com.example.orders.application;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.orders.adapters.InMemoryOrderRepository;
import com.example.orders.domain.Clock;
import com.example.orders.domain.Money;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderConfirmed;
import com.example.orders.domain.OrderId;
import com.example.orders.domain.OrderItem;
import com.example.orders.domain.OrderLine;
import com.example.orders.domain.OrderNotFoundException;
import com.example.orders.domain.OrderRepository;
import com.example.orders.domain.OrderStatus;
import com.example.orders.domain.OrderValidationException;
import com.example.orders.domain.PaymentRecorded;
import com.example.orders.domain.StandardOrder;
import com.example.orders.domain.BaseOrder;

class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository repository;
    private List<OrderId> notifiedOrders;
    private NotificationPort notificationPort;
    private Clock fixedClock;
    private Instant fixedInstant;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        repository = new InMemoryOrderRepository();
        notifiedOrders = new ArrayList<>();
        notificationPort = notifiedOrders::add;
        fixedInstant = Instant.parse("2026-10-05T12:00:00Z");
        fixedClock = () -> fixedInstant;
    }

    @Test
    @DisplayName("Should throw OrderValidationException when adding order with empty items")
    void testAddOrderValidationException() {
        StandardOrder emptyOrder = new StandardOrder("ORD-01", LocalDate.now(), OrderStatus.PENDING, List.of(), 0.0);
        assertThrows(OrderValidationException.class, () -> orderService.addOrder(emptyOrder));
    }

    @Test
    @DisplayName("Should throw OrderNotFoundException when order does not exist")
    void testFindOrderNotFoundException() {
        assertThrows(OrderNotFoundException.class, () -> orderService.findById("UNKNOWN"));
    }

    @Test
    @DisplayName("Should filter orders by status")
    void testFilterAndSortOrders() throws OrderValidationException {
        OrderItem item1 = new OrderItem("ITM-01", "Item A", 100.0, 1);
        OrderItem item2 = new OrderItem("ITM-02", "Item B", 250.0, 2);

        StandardOrder order1 = new StandardOrder("ORD-01", LocalDate.now(), OrderStatus.CONFIRMED, List.of(item1), 20.0);
        StandardOrder order2 = new StandardOrder("ORD-02", LocalDate.now(), OrderStatus.CONFIRMED, List.of(item2), 50.0);
        StandardOrder order3 = new StandardOrder("ORD-03", LocalDate.now(), OrderStatus.DRAFT, List.of(item1), 0.0);

        orderService.addOrder(order1);
        orderService.addOrder(order2);
        orderService.addOrder(order3);

        List<BaseOrder> result = orderService.filterByStatus(OrderStatus.CONFIRMED);

        assertEquals(2, result.size());
        assertEquals("ORD-01", result.get(0).getOrderId());
        assertEquals("ORD-02", result.get(1).getOrderId());
    }
    

    @Test
    @DisplayName("Full workflow: create -> add item -> confirm -> record payment")
    void shouldCompleteFullOrderLifecycle() {
        OrderId id = OrderId.generate();
        Order order = new Order(id); 
        order.addLine(new OrderLine("ITEM-1", Money.of("1200.00"), 1));
        repository.save(order);

        ConfirmOrderService confirmService = new ConfirmOrderService(repository, notificationPort, fixedClock);
        OrderConfirmed confirmEvent = confirmService.confirm(id);
        assertNotNull(confirmEvent);
        assertEquals(1, notifiedOrders.size());
        assertEquals(id, notifiedOrders.get(0));

        RecordPaymentService paymentService = new RecordPaymentService(repository, fixedClock);
        PaymentRecorded paymentEvent = paymentService.recordPayment(id);
        assertNotNull(paymentEvent);

        Order finalOrder = repository.findById(id).orElseThrow();
        assertEquals(OrderStatus.PAID, finalOrder.getStatus());
    }
}
