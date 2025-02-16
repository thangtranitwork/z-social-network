package com.thangtranit.mailservice.repository.httpclient;

import com.thangtranit.mailservice.dto.request.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "brevo-client", url = "https://api.brevo.com")
@Component
public interface BrevoClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    Object sendSmtpEmail(@RequestHeader("api-key") String key, @RequestBody EmailRequest body);
}
