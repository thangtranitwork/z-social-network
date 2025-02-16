package com.thangtranit.mailservice.dto.request;

import java.util.List;


public record EmailRequest(
        Sender sender,
        List<Recipient> to,
        String htmlContent,
        String subject
) { }
