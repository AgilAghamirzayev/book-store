package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String entity, Object id) {
        super(String.format("%s not found by %s", entity, id));
    }

    public UserNotFoundException(Long id) {
        super(String.format("User not found by id: %s", id));
    }
}