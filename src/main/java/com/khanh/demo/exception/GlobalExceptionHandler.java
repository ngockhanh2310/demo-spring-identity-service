package com.khanh.demo.exception;

import com.khanh.demo.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleException(Exception ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()); // Custom code error
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage()); // Notify message error
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
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = IllegalStateException.class)
    ResponseEntity<ApiResponse> handleIllegalStateException(IllegalStateException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = ex.getBindingResult()
                    .getAllErrors()
                    .getFirst()
                    .unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
        } catch (IllegalArgumentException e) {
            e.getMessage();
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode()); // Custom code error
        apiResponse.setMessage(
                Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage()
        ); // Notify message error
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue)
                .replace("{" + MAX_ATTRIBUTE + "}", maxValue);
    }
    //@ExceptionHandler(value = MethodArgumentNotValidException.class)
    //ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException ex) {
    //    Map<String, String> errors = new HashMap<>();
    //
    //    ex.getBindingResult().getAllErrors().forEach(error -> {
    //        String fieldName = ((FieldError) error).getField();
    //        String enumKey = error.getDefaultMessage();
    //
    //        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;
    //        String errorMessage = enumKey;
    //
    //        try {
    //            errorCode = ErrorCode.valueOf(enumKey);
    //
    //            var constraintViolation = error.unwrap(ConstraintViolation.class);
    //            Map<String, Object> attributes = constraintViolation
    //                    .getConstraintDescriptor()
    //                    .getAttributes();
    //
    //            log.info("Field: {}, Attributes: {}", fieldName, attributes);
    //
    //            // Map dynamic attributes vào message
    //            errorMessage = mapAttribute(errorCode.getMessage(), attributes);
    //
    //        } catch (IllegalArgumentException e) {
    //            log.error("Cannot parse error code: {}", enumKey);
    //            errorMessage = errorCode.getMessage();
    //        } catch (Exception e) {
    //            log.error("Error processing validation: {}", e.getMessage());
    //            errorMessage = errorCode.getMessage();
    //        }
    //
    //        errors.put(fieldName, errorMessage);
    //    });
    //
    //    ApiResponse apiResponse = new ApiResponse();
    //    apiResponse.setCode(ErrorCode.INVALID_KEY.getCode()); // Hoặc tạo ErrorCode.VALIDATION_ERROR
    //    apiResponse.setMessage("Validation failed");
    //    apiResponse.setResult(errors); // Đưa tất cả errors vào result
    //
    //    return ResponseEntity.badRequest().body(apiResponse);
    //}
    //
    //private String mapAttribute(String message, Map<String, Object> attributes) {
    //    String result = message;
    //
    //    // Lặp qua tất cả attributes và replace vào message
    //    for (Map.Entry<String, Object> entry : attributes.entrySet()) {
    //        String key = entry.getKey();
    //        String value = String.valueOf(entry.getValue());
    //        String placeholder = "{" + key + "}";
    //
    //        if (result.contains(placeholder)) {
    //            result = result.replace(placeholder, value);
    //        }
    //    }
    //
    //    return result;
    //}
}
