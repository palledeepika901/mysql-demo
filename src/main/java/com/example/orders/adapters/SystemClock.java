package com.example.orders.adapters;

import com.example.orders.domain.Clock;
import java.time.Instant;

public class SystemClock implements Clock {
    @Override
    public Instant now() {
        return Instant.now();
    }
}