package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyEmailRegisterException extends RuntimeException {

    private final HttpStatus status;

    public AlreadyEmailRegisterException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
