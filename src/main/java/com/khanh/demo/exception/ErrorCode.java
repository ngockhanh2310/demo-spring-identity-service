package com.khanh.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_EXIST(1002, "User exists", HttpStatus.BAD_REQUEST),
    ERROR_CODE_EXCEPTION(1001, "Error code exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003, "Username must be between {min} and {max} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be between {min} and {max} characters", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(1005, "Invalid message", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_DOB(1008, "User must be at least {min} years old", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1007, "You do not have access to this resource", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(1009, "User not found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
