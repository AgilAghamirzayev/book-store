package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotEnabledException extends RuntimeException {

    public UserNotEnabledException(String entity) {
        super(String.format("%s not enabled, please activate your registration from your email.", entity));
    }

}
