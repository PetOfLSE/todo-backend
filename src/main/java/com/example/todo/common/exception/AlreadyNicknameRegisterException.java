package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class AlreadyNicknameRegisterException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public AlreadyNicknameRegisterException(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
