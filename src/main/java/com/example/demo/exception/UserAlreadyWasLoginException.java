package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class UserAlreadyWasLoginException extends RuntimeException {

    public UserAlreadyWasLoginException(String entity) {
        super(String.format("%s already was login", entity));
    }

}