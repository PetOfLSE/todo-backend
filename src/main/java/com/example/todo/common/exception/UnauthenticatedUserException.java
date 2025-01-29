package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthenticatedUserException extends RuntimeException {

    private final HttpStatus status;

    public UnauthenticatedUserException(String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
