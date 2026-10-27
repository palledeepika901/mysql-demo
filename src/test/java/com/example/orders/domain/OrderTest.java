
    package com.example.orders.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
// Included because your other tests use it
import org.junit.jupiter.api.DisplayName;


class OrderTest {
	// Restore the original constructor for the test suite
    

    @Test
    @DisplayName("Rule 1: An order must contain at least one line before confirmation")
    void shouldRejectConfirmationWithoutLines() {
        Order order = new Order(OrderId.generate());

        IllegalStateException ex = assertThrows(IllegalStateException.class, order::confirm);
        assertTrue(ex.getMessage().contains("at least one line before confirmation"));
    }
    

    @Test
    @DisplayName("Rule 1: Confirmation succeeds when order has lines")
    void shouldConfirmOrderWithLines() {
        Order order = new Order(OrderId.generate());
        order.addLine(new OrderLine("PROD-1", Money.of("100.00"), 1));

        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("Rule 2: Quantity must be a positive whole number")
    void shouldRejectZeroOrNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () ->
                new OrderLine("PROD-1", Money.of("50.00"), 0));

        assertThrows(IllegalArgumentException.class, () ->
                new OrderLine("PROD-1", Money.of("50.00"), -3));
    }

    @Test
    @DisplayName("Rule 3: A cancelled order cannot be paid")
    void shouldRejectPaymentOnCancelledOrder() {
        Order order = new Order(OrderId.generate());
        order.addLine(new OrderLine("PROD-1", Money.of("50.00"), 2));
        order.cancel("Customer cancellation");

        IllegalStateException ex = assertThrows(IllegalStateException.class, order::recordPayment);
        assertTrue(ex.getMessage().contains("cancelled order cannot be paid"));
    }

    @Test
    @DisplayName("Rule 4: A paid order cannot return to draft or be cancelled")
    void shouldRejectCancellationOnPaidOrder() {
        Order order = new Order(OrderId.generate());
        order.addLine(new OrderLine("PROD-1", Money.of("25.00"), 2));
        order.confirm();
        order.recordPayment();

        assertEquals(OrderStatus.PAID, order.getStatus());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> order.cancel("Refund request"));
        assertTrue(ex.getMessage().contains("paid order cannot return to draft or be cancelled"));
    }

    @Test
    @DisplayName("Rule 5: Order total is derived from immutable line prices and quantities")
    void shouldCalculateDerivedTotalCorrectly() {
        Order order = new Order(OrderId.generate());
        order.addLine(new OrderLine("ITEM-A", Money.of("20.00"), 2)); // 40.00
        order.addLine(new OrderLine("ITEM-B", Money.of("15.50"), 3)); // 46.50

        // 40.00 + 46.50 = 86.50
        assertEquals(Money.of("86.50"), order.total());
    }
}