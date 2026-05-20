package com.prakash.product_service.messaging;

import com.prakash.product_service.config.RabbitMqProperties;
import com.prakash.product_service.dto.MailRequest;
import com.prakash.product_service.event.ProductCreatedEvent;
import com.prakash.product_service.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEventListener {
    private final MailService mailService;
    private final RabbitMqProperties rabbitMqProperties;

    public ProductEventListener(MailService mailService, RabbitMqProperties rabbitMqProperties) {
        this.mailService = mailService;
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @RabbitListener(
            queues = "${app.rabbitmq.product-created-queue}",
            autoStartup = "${app.rabbitmq.listener-auto-startup:false}"
    )
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("Received product created event {} for product id {}", event.eventId(), event.productId());

        String notificationEmail = rabbitMqProperties.notificationEmail();
        if (notificationEmail == null || notificationEmail.isBlank()) {
            log.info("Skipping product created notification because app.rabbitmq.notification-email is not configured");
            return;
        }
        log.info("Sending product created notification to {}", notificationEmail);
        mailService.sendMail(MailRequest.builder()
                .to(notificationEmail)
                .subject("Product created: " + event.name())
                .message(buildMessage(event))
                .build());
        log.info("Product created notification sent for event {} to {}", event.eventId(), notificationEmail);
    }

    private String buildMessage(ProductCreatedEvent event) {
        return """
                A product was created.

                Event ID: %s
                ID: %s
                Name: %s
                Description: %s
                Price: %s
                Quantity: %s
                Created At: %s
                """.formatted(
                event.eventId(),
                event.productId(),
                event.name(),
                event.description(),
                event.price(),
                event.quantity(),
                event.createdAt()
        );
    }
}
