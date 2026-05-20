package com.prakash.product_service.event;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductCreatedEvent(
        String eventId,
        Long productId,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Instant createdAt
) {
}
