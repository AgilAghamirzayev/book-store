package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException(String entity) {
        super(String.format("token: %s not valid", entity));
    }

}
