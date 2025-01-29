package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TokenNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public TokenNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }
}
