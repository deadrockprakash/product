package com.prakash.product_service.service;

import com.prakash.product_service.dto.MailRequest;

public interface MailService {
    void sendMail(MailRequest mailRequest);
}
