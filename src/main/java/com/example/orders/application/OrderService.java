package com.example.orders.application;
import com.example.orders.domain.Order;
import com.example.orders.domain.OrderStatus; // Required for the OrderStatus enum
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    // Inject the interface, NOT the implementation
    private final OrderRepositoryPort orderRepository;

    public OrderService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createNewOrder(String status, String productName, int quantity, double price) {
        // Core business logic (domain)
        Order newOrder = new Order();

        // Converts the String status into the OrderStatus enum
        newOrder.setStatus(OrderStatus.valueOf(status));
        
        // Map the new fields to the entity
        newOrder.setProductName(productName);
        newOrder.setQuantity(quantity);
        newOrder.setPrice(price);

        /* 
         * NOTE: If you updated your Order constructor to accept all fields at once 
         * as suggested in the tutorial, you can replace the 5 lines above with:
         * Order newOrder = new Order(OrderStatus.valueOf(status), productName, quantity, price);
         */

        // Save using the port
        orderRepository.save(newOrder);
    }

    // Note: If you want to keep your previous addOrder, filterByStatus,
    // or getOrdersSortedByTotal methods, you will need to rewrite them
    // to call methods on 'orderRepository' instead of the deleted 'orders' ArrayList
}