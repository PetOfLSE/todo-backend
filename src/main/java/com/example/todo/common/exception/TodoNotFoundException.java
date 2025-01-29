package com.example.todo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TodoNotFoundException extends RuntimeException{

    private final HttpStatus status;

    public TodoNotFoundException(String message){
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }
}
