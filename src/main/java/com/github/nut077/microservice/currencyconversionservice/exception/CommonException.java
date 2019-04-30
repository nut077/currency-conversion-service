package com.github.nut077.microservice.currencyconversionservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends RuntimeException {

    protected HttpStatus status;
    protected String code;

    public CommonException(String message) {
        super(message);
    }
}
