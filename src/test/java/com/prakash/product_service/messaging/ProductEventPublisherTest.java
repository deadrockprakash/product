package com.prakash.product_service.messaging;

import com.prakash.product_service.config.RabbitMqProperties;
import com.prakash.product_service.event.ProductCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.net.ConnectException;
import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductEventPublisherTest {
    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    RabbitMqProperties rabbitMqProperties;

    @InjectMocks
    ProductEventPublisher productEventPublisher;

    @Test
    void publishProductCreated_ShouldPublishToConfiguredExchangeAndRoutingKey() {
        ProductCreatedEvent event = new ProductCreatedEvent(
                "event-1",
                1L,
                "Laptop",
                "Laptop description",
                new BigDecimal("50000.00"),
                4,
                Instant.parse("2026-05-17T00:00:00Z")
        );
        when(rabbitMqProperties.productExchange()).thenReturn("product.exchange");
        when(rabbitMqProperties.productCreatedRoutingKey()).thenReturn("product.created");

        productEventPublisher.publishProductCreated(event);

        verify(rabbitTemplate).convertAndSend(
                eq("product.exchange"),
                eq("product.created"),
                eq(event),
                argThat((CorrelationData correlationData) -> "event-1".equals(correlationData.getId()))
        );
    }

    @Test
    void publishProductCreated_ShouldNotFail_WhenRabbitMqUnavailableAndFailFastDisabled() {
        ProductCreatedEvent event = event();
        when(rabbitMqProperties.productExchange()).thenReturn("product.exchange");
        when(rabbitMqProperties.productCreatedRoutingKey()).thenReturn("product.created");
        when(rabbitMqProperties.publishFailFast()).thenReturn(false);
        doThrow(new AmqpConnectException(new ConnectException("Connection refused")))
                .when(rabbitTemplate).convertAndSend(
                        eq("product.exchange"),
                        eq("product.created"),
                        eq(event),
                        any(CorrelationData.class)
                );

        assertDoesNotThrow(() -> productEventPublisher.publishProductCreated(event));
    }

    @Test
    void publishProductCreated_ShouldFail_WhenRabbitMqUnavailableAndFailFastEnabled() {
        ProductCreatedEvent event = event();
        when(rabbitMqProperties.productExchange()).thenReturn("product.exchange");
        when(rabbitMqProperties.productCreatedRoutingKey()).thenReturn("product.created");
        when(rabbitMqProperties.publishFailFast()).thenReturn(true);
        doThrow(new AmqpConnectException(new ConnectException("Connection refused")))
                .when(rabbitTemplate).convertAndSend(
                        eq("product.exchange"),
                        eq("product.created"),
                        eq(event),
                        any(CorrelationData.class)
                );

        assertThrows(AmqpConnectException.class, () -> productEventPublisher.publishProductCreated(event));
    }

    private ProductCreatedEvent event() {
        return new ProductCreatedEvent(
                "event-1",
                1L,
                "Laptop",
                "Laptop description",
                new BigDecimal("50000.00"),
                4,
                Instant.parse("2026-05-17T00:00:00Z")
        );
    }
}
