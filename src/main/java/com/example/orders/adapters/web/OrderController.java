
package com.example.orders.adapters.web;

import com.example.orders.application.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid; // Or javax.validation.Valid depending on your Spring Boot version

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // Inject the application service
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        // The REST controller calls the service in the application package
        // Update the method call to pass all the extracted data
        orderService.createNewOrder(
            requestDTO.getStatus(),
            requestDTO.getProductName(),
            requestDTO.getQuantity(),
            requestDTO.getPrice()
        );

        return "Order successfully created with status: " + requestDTO.getStatus();
    }
}