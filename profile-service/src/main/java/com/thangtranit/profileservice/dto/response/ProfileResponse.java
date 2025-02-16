package com.thangtranit.profileservice.dto.response;

import com.thangtranit.profileservice.enums.Gender;

import java.time.LocalDate;

public record ProfileResponse(
        String id,
        String familyName,
        String givenName,
        Gender gender,
        LocalDate birthdate,
        String profilePicture,
        String coverPicture,
        String bio
        ) {
}
