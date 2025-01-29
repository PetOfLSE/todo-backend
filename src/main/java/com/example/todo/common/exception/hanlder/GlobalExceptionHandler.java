package com.example.todo.common.exception.hanlder;

import com.example.todo.common.exception.*;
import com.example.todo.common.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UnauthenticatedUserException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthenticatedUserException(UnauthenticatedUserException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = TodoNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTodoNotFoundException(TodoNotFoundException e){
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ExpiredTokenException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredTokenException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(value = HasNotRoleTokenException.class)
    public ResponseEntity<ExceptionResponse> handleHasNotRoleTokenException(HasNotRoleTokenException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(value = TokenNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTokenNotFoundException(TokenNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(e.getStatus().value())
                .build();

        return new ResponseEntity<>(response, e.getStatus());
    }

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

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .message(e.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
