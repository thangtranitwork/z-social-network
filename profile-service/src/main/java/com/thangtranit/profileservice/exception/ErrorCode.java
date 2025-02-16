package com.thangtranit.profileservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    PROFILE_ALREADY_EXISTS(4000, "Profile has already exists", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_FOUND(4001, "Profile has not exists", HttpStatus.NOT_FOUND),
    NO_RESOURCE_FOUND(4002, "No resource found", HttpStatus.BAD_REQUEST),
    UPLOAD_PROFILE_PICTURE_FAILED(4003, "Upload profile picture failed", HttpStatus.BAD_REQUEST),
    UPLOAD_COVER_PICTURE_FAILED(4004, "Upload cover picture failed", HttpStatus.BAD_REQUEST),
    FAMILY_NAME_IS_INVALID(4005, "Family name must be at least 1 - 100 characters long", HttpStatus.BAD_REQUEST),
    GIVEN_NAME_IS_INVALID(4006, "Given name must be at least 1 - 100 characters", HttpStatus.BAD_REQUEST),
    FAMILY_NAME_IS_REQUIRED(4007, "Family name is required", HttpStatus.BAD_REQUEST),
    GIVEN_NAME_IS_REQUIRED(4008, "Given name is required", HttpStatus.BAD_REQUEST),
    CHANGE_NAME_FAILED_BECAUSE_IN_COUNT_DOWN(4006, "Change name failed because in count down", HttpStatus.BAD_REQUEST),
    CHANGE_BIRTH_DATE_FAILED_BECAUSE_IN_COUNT_DOWN(4007, "Change birthdate failed because in count down", HttpStatus.BAD_REQUEST),
    BIRTHDATE_IS_REQUIRED(4008, "Birthdate is required", HttpStatus.BAD_REQUEST),
    USER_MUST_BE_OVER_16_YEARS_OLD(4009, "User must be over 16 years old", HttpStatus.BAD_REQUEST),
    BLOCKED(4007, "You are blocked this user or this user is blocked you", HttpStatus.FORBIDDEN),
    PERMISSION_DENIED(4998, "You do not have permission to view, modify, or share this resource", HttpStatus.FORBIDDEN),
    UNCATEGORIZED_ERROR(4999, "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1998, "Unauthenticated", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
