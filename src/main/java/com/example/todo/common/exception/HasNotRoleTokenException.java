package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HasNotRoleTokenException extends RuntimeException {

    private final HttpStatus status;

    public HasNotRoleTokenException(String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
