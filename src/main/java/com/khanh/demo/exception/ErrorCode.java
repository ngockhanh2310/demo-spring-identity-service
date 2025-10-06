package com.khanh.demo.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {
    USERNAME_EXIST(1002, "User exists"),
    ERROR_CODE_EXCEPTION(9999, "Error code exception"),
    USERNAME_INVALID(1001, "Username invalid"),
    PASSWORD_INVALID(1003, "Password invalid"),
    INVALID_MESSAGE_KEY(1004, "Invalid message"),
    UNAUTHENTICATED(1005, "Unauthenticated"),
    
    ;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
