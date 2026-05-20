package com.prakash.product_service.messaging;

import com.prakash.product_service.config.RabbitMqProperties;
import com.prakash.product_service.dto.MailRequest;
import com.prakash.product_service.event.ProductCreatedEvent;
import com.prakash.product_service.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductEventListenerTest {
    @Mock
    MailService mailService;

    @Mock
    RabbitMqProperties rabbitMqProperties;

    @InjectMocks
    ProductEventListener productEventListener;

    @Test
    void handleProductCreated_ShouldSendNotificationEmail_WhenEmailConfigured() {
        ProductCreatedEvent event = event();
        when(rabbitMqProperties.notificationEmail()).thenReturn("admin@example.com");

        productEventListener.handleProductCreated(event);

        verify(mailService).sendMail(argThat(request ->
                "admin@example.com".equals(request.getTo())
                        && "Product created: Laptop".equals(request.getSubject())
                        && request.getMessage().contains("Event ID: event-1")
                        && request.getMessage().contains("ID: 1")
                        && request.getMessage().contains("Name: Laptop")
        ));
    }

    @Test
    void handleProductCreated_ShouldSkipEmail_WhenEmailNotConfigured() {
        when(rabbitMqProperties.notificationEmail()).thenReturn(" ");

        productEventListener.handleProductCreated(event());

        verify(mailService, never()).sendMail(org.mockito.ArgumentMatchers.any(MailRequest.class));
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
