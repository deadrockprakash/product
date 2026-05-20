package com.prakash.product_service.service;

import com.prakash.product_service.dto.MailRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(MailRequest mailRequest) {
        log.info("Sending mail to {}", mailRequest.getTo());
        log.info("Subject: {}", mailRequest.getSubject());
        log.info("Message: {}", mailRequest.getMessage());
        log.info("Sending mail...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getTo());
        message.setSubject(mailRequest.getSubject());
        message.setText(mailRequest.getMessage());
        try{
            javaMailSender.send(message);
        }catch(Exception e) {
            log.error("Error sending mail {}", e.getMessage());
        }

        log.info("Mail sent successfully");
    }
}
