package com.thangtranit.mailservice.controller;

import com.thangtranit.mailservice.dto.request.EmailRequest;
import com.thangtranit.mailservice.dto.response.ApiResponse;
import com.thangtranit.mailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    public ApiResponse<Object> sendEmail(@RequestBody EmailRequest request) {
        return ApiResponse.builder()
                .body(emailService.send(request))
                .build();
    }
}
