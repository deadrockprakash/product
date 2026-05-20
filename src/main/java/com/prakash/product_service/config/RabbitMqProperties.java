package com.prakash.product_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbitmq")
public record RabbitMqProperties(
        String productExchange,
        String productCreatedQueue,
        String productCreatedRoutingKey,
        String deadLetterExchange,
        String productCreatedDeadLetterQueue,
        String productCreatedDeadLetterRoutingKey,
        String notificationEmail,
        boolean publishFailFast,
        int listenerRetryMaxAttempts,
        long listenerRetryInitialInterval,
        double listenerRetryMultiplier,
        long listenerRetryMaxInterval
) {
}
