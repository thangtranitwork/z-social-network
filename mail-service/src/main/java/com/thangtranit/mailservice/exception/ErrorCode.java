package com.thangtranit.mailservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CAN_NOT_SEND_EMAIL(3000, "Can not send email", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_ERROR(3999, "Something went wrong", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
