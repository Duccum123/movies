package com.example.phimmoi.exception;


import com.example.phimmoi.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<String>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(999);
        apiResponse.setMessage(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> handlingAppException(AppException exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(exception.getErrorCode().getCode());
        apiResponse.setMessage(exception.getErrorCode().getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
