package com.prakash.product_service.service;

import com.prakash.product_service.dto.MailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    MailServiceImpl mailService;

    @Test
    void sendMail_ShouldSendSimpleMailMessage() {
        MailRequest request = MailRequest.builder()
                .to("user@example.com")
                .subject("Product update")
                .message("New product is available")
                .build();
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        mailService.sendMail(request);

        verify(javaMailSender).send(captor.capture());
        SimpleMailMessage sentMessage = captor.getValue();
        assertArrayEquals(new String[]{"user@example.com"}, sentMessage.getTo());
        assertEquals("Product update", sentMessage.getSubject());
        assertEquals("New product is available", sentMessage.getText());
    }
}
