package com.prakash.product_service.messaging;

import com.prakash.product_service.config.RabbitMqProperties;
import com.prakash.product_service.event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate, RabbitMqProperties rabbitMqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMqProperties = rabbitMqProperties;
    }

    public void publishProductCreated(ProductCreatedEvent event) {
        try {
            log.info("Publishing product created event {} for product id {}", event.eventId(), event.productId());
            rabbitTemplate.convertAndSend(
                    rabbitMqProperties.productExchange(),
                    rabbitMqProperties.productCreatedRoutingKey(),
                    event,
                    new CorrelationData(event.eventId())
            );
        } catch (AmqpException exception) {
            if (rabbitMqProperties.publishFailFast()) {
                throw exception;
            }
            log.warn("Product created event was not published because RabbitMQ is unavailable: {}", exception.getMessage());
        }
    }
}
