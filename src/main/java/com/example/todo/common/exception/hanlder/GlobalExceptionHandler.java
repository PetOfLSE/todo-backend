package com.example.todo.common.exception.hanlder;

import com.example.todo.common.exception.AlreadyEmailRegisterException;
import com.example.todo.common.exception.AlreadyNicknameRegisterException;
import com.example.todo.common.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AlreadyEmailRegisterException.class)
    public ResponseEntity<ExceptionResponse> alreadyEmailRegisterException(AlreadyEmailRegisterException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(value = AlreadyNicknameRegisterException.class)
    public ResponseEntity<ExceptionResponse> alreadyNicknameRegisterException(AlreadyNicknameRegisterException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message("알 수 없는 예외 발생")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
