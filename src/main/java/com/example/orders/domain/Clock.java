package com.example.orders.domain;

import java.time.Instant;

public interface Clock {
    Instant now();
}