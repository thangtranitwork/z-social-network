package com.thangtranit.profileservice.dto.response;

public record IntrospectResponse(
        boolean valid,
        String id,
        String role
) {
}
