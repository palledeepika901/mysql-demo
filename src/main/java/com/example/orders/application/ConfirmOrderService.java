//package com.example.orders.application;
//
//import com.example.orders.domain.Clock;
//import com.example.orders.domain.Order;
//import com.example.orders.domain.OrderConfirmed;
//import com.example.orders.domain.OrderId;
//import com.example.orders.domain.OrderRepository;
//
//import java.util.NoSuchElementException;
//
//public class ConfirmOrderService {
//
//    private final OrderRepository repository;
//    private final Clock clock;
//
//    public ConfirmOrderService(OrderRepository repository, Clock clock) {
//        this.repository = repository;
//        this.clock = clock;
//    }
//
//    public OrderConfirmed confirm(OrderId id) {
//        Order order = repository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id.value()));
//
//        order.confirm();
//        repository.save(order);
//
//        return new OrderConfirmed(order.getId(), clock.now());
//    }
//}
package com.example.orders.application;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.example.orders.domain.Clock;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderConfirmed;
import com.example.orders.domain.OrderId;
import com.example.orders.domain.OrderRepository;

public class ConfirmOrderService {

    private final OrderRepository repository;
    private final NotificationPort notificationPort;
    private final Clock clock;

    // Constructor supporting repository and notificationPort (as used in tests)
    public ConfirmOrderService(OrderRepository repository, NotificationPort notificationPort) {
        this(repository, notificationPort, () -> java.time.Instant.now());
    }

    // Full constructor including clock
    public ConfirmOrderService(OrderRepository repository, NotificationPort notificationPort, Clock clock) {
        this.repository = Objects.requireNonNull(repository, "repository cannot be null");
        this.notificationPort = notificationPort;
        this.clock = clock;
    }

    public OrderConfirmed confirm(OrderId id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id.value()));

        order.confirm();
        repository.save(order);

        OrderConfirmed event = new OrderConfirmed(order.getId(), clock.now());
        if (notificationPort != null) {
            notificationPort.sendOrderApproved(order.getId());
        }
        
        return event;
    }
}