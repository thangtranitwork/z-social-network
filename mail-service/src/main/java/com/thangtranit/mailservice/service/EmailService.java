package com.thangtranit.mailservice.service;

import com.thangtranit.mailservice.dto.request.EmailRequest;
import com.thangtranit.mailservice.dto.request.Sender;
import com.thangtranit.mailservice.exception.AppException;
import com.thangtranit.mailservice.exception.ErrorCode;
import com.thangtranit.mailservice.repository.httpclient.BrevoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final BrevoClient brevoClient;
    @Value("${brevo.api-key}")
    private String apiKey;
    @Value("${brevo.email}")
    private String email;
    @Value(("${brevo.name}"))
    private String name;
    public Object send(EmailRequest request) {
        try {
            Sender sender = new Sender(name, email);
            EmailRequest emailRequest = new EmailRequest(
                    sender,
                    request.to(),
                    request.htmlContent(),
                    request.subject());
            return brevoClient.sendSmtpEmail(apiKey, emailRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ErrorCode.CAN_NOT_SEND_EMAIL);
        }
    }
}
