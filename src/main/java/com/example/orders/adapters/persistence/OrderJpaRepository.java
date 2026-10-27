package com.example.orders.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    // Add custom @Query methods here as required by the task[span_14](start_span)[span_14](end_span)
}