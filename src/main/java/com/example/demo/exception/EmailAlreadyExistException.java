package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String entity) {
        super(String.format("%s already exist.", entity));
    }
}
