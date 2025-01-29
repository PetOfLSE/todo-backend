package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyNicknameRegisterException extends RuntimeException {

    private final HttpStatus status;

    public AlreadyNicknameRegisterException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
