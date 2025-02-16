package com.thangtranit.profileservice.dto.request;

import com.thangtranit.profileservice.entity.Profile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeNameRequest(
        @NotNull(message = "FAMILY_NAME_IS_REQUIRED")
        @Size(min = 1, max = Profile.NAME_MAX_LENGTH, message = "FAMILY_NAME_IS_INVALID")
        String familyName,
        @NotNull(message = "GIVEN_NAME_IS_REQUIRED")
        @Size(min = 1, max = Profile.NAME_MAX_LENGTH, message = "GIVEN_NAME_IS_INVALID")
        String givenName
) {
}
