package com.example.orders.adapters.persistence;

import org.springframework.stereotype.Component;
import com.example.orders.domain.Order;
import com.example.orders.application.OrderRepositoryPort;

@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpaRepository;

    public OrderPersistenceAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Order domainOrder) {
        // Map from pure domain object to JPA entity
        OrderJpaEntity entity = new OrderJpaEntity();
        
        // Convert the OrderStatus enum to a String using .name()
        entity.setStatus(domainOrder.getStatus().name());

        // Use the JpaRepository to actually save data to the database
        jpaRepository.save(entity);
    }
}