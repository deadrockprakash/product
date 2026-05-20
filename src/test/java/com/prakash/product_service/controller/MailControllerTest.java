package com.prakash.product_service.controller;

import com.prakash.product_service.dto.MailRequest;
import com.prakash.product_service.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailControllerTest {
    @Mock
    MailService mailService;

    @InjectMocks
    MailController mailController;

    @Test
    void sendMail_ShouldReturnSuccessMessage() {
        MailRequest request = MailRequest.builder()
                .to("user@example.com")
                .subject("Product update")
                .message("New product is available")
                .build();

        ResponseEntity<String> response = mailController.sendMail(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mail sent successfully", response.getBody());
        verify(mailService).sendMail(request);
    }
}
