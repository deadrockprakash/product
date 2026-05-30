package com.prakash.product_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request object for sending an email")
public class MailRequest {
    @Schema(description = "Recipient email address", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Recipient email must be valid")
    private String to;

    @Schema(description = "Email subject", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email subject is required")
    private String subject;

    @Schema(description = "Plain text email body", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email message is required")
    private String message;
}
