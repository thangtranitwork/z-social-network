package com.thangtranit.apigateway.dto.response;

public record IntrospectResponse(
        boolean valid,
        String id,
        String role
) { }
