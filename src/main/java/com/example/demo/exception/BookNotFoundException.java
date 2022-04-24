package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String entity) {
        super(String.format("Book not found by title: ", entity));
    }

    public BookNotFoundException(Long id) {
        super(String.format("Book not found by id: %s", id));
    }
}
