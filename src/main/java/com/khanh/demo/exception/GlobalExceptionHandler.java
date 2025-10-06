package com.khanh.demo.exception;

import com.khanh.demo.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleException(Exception ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.ERROR_CODE_EXCEPTION.getCode()); // Custom code error
        apiResponse.setMessage(ErrorCode.ERROR_CODE_EXCEPTION.getMessage()); // Notify message error
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(1001); // Custom code error
        apiResponse.setMessage(ex.getMessage()); // Notify message error
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode()); // Custom code error
        apiResponse.setMessage(errorCode.getMessage()); // Notify message error
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String enumKey = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            e.getMessage();
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode()); // Custom code error
        apiResponse.setMessage(errorCode.getMessage()); // Notify message error
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
