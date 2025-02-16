package com.thangtranit.relationshipservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CAN_NOT_MAKE_A_RELATIONSHIP_WITH_YOURSELF(5000, "Can't make a relationship with yourself", HttpStatus.BAD_REQUEST),
    RELATIONSHIP_NOT_FOUND(5001, "Relationship not found", HttpStatus.NOT_FOUND),
    HAVE_NOT_BLOCKED(5002, "Have not blocked", HttpStatus.BAD_REQUEST),
    HAVE_ALREADY_BE_FRIEND(5003, "Have already be friend", HttpStatus.BAD_REQUEST),
    HAVE_ALREADY_BLOCKED(5004, "Have already blocked", HttpStatus.BAD_REQUEST),
    HAVE_NOT_ALREADY_BE_FRIEND(5005, "Have not be friend", HttpStatus.BAD_REQUEST),
    HAVE_ALREADY_SENT_ADD_FRIEND_INVITATION(5006, "Have already sent add friend invitation", HttpStatus.BAD_REQUEST),
    ADD_FRIEND_INVITATION_NOT_FOUND(5007, "Add friend invitation not found", HttpStatus.BAD_REQUEST),
    ACCEPT_ADD_FRIEND_INVITATION_FAILED(5008, "Accept add friend invitation failed", HttpStatus.BAD_REQUEST),
    REJECT_ADD_FRIEND_INVITATION_FAILED(5009, "Reject add friend invitation failed", HttpStatus.BAD_REQUEST),
    CANCEL_ADD_FRIEND_INVITATION_FAILED(5010, "Cancel add friend invitation failed", HttpStatus.BAD_REQUEST),
    NO_RESOURCE_FOUND(5997, "No resource found", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_ERROR(5999, "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1998, "Unauthenticated", HttpStatus.UNAUTHORIZED),;

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
