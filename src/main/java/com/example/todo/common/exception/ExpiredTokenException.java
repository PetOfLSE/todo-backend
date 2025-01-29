package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExpiredTokenException extends RuntimeException {

    private final HttpStatus status;

    public ExpiredTokenException(String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
