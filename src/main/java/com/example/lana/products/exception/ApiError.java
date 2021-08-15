package com.example.lana.products.exception;

import lombok.Getter;

@Getter
public class ApiError {
    private String error;
    private String message;
    private Integer status;

    public ApiError(String error, String message, Integer status) {
        super();
        this.error = error;
        this.message = message;
        this.status = status;
    }
}
