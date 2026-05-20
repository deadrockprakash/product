package com.prakash.product_service.controller;

import com.prakash.product_service.dto.MailRequest;
import com.prakash.product_service.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyAuthority('ADD')")
    public ResponseEntity<String> sendMail(@RequestBody MailRequest mailRequest) {
        mailService.sendMail(mailRequest);
        return new ResponseEntity<>("Mail sent successfully", HttpStatus.OK);
    }
}
