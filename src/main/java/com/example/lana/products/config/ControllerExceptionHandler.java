package com.example.lana.products.config;

import com.example.lana.products.exception.ApiError;
import com.example.lana.products.exception.BasketNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {BasketNotFoundException.class})
    public ResponseEntity<ApiError> basketNotFoundException(BasketNotFoundException ex) {
        log.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Basket Not Found Exception", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ApiError> deserializingRequestException(HttpMessageNotReadableException ex) {
        log.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Deserializing Request Exception", ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn(String.format("Exception %s was thrown", ex.getClass()));

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Method Argument Not Valid");

        //Get all errors
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn(String.format("Errors: %s", errors));

        body.put("errors", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiError> handleUnknownException(Exception ex) {
        log.warn(String.format("Exception %s was thrown with message: %s", ex.getClass(), ex.getMessage()));
        ApiError apiError = new ApiError("Internal Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(apiError.getStatus())
                .body(apiError);
    }

}
