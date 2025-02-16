package com.thangtranit.profileservice.dto.response;

public record ProfileMinimumResponse(
        String id,
        String familyName,
        String givenName,
        String profilePicture
) {
}
