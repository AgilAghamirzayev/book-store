package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class TokenNotConfirmedException extends RuntimeException {
    public TokenNotConfirmedException(String message) {
        super(message);
    }
}
