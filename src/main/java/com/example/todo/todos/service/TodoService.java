package com.example.todo.todos.service;

import com.example.todo.auth.controller.response.UserResponse;
import com.example.todo.auth.persistence.entity.UserEntity;
import com.example.todo.auth.persistence.repository.UserEntityRepository;
import com.example.todo.common.custom.CustomUserDetails;
import com.example.todo.common.exception.TodoNotFoundException;
import com.example.todo.common.exception.UnauthenticatedUserException;
import com.example.todo.common.exception.UserNotFoundException;
import com.example.todo.todos.controller.request.TodoAddRequest;
import com.example.todo.todos.controller.response.TodoResponse;
import com.example.todo.todos.persistence.entity.TodoEntity;
import com.example.todo.todos.persistence.repository.TodoEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoEntityRepository todoEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public TodoResponse add(CustomUserDetails userDetails, TodoAddRequest request) {
        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없음"));

        TodoEntity todo = TodoEntity.builder()
                .content(request.getContent())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        user.getTodo().add(todo);

        TodoEntity saveTodo = todoEntityRepository.save(todo);
        userEntityRepository.save(user);

        return TodoResponse.builder()
                .id(saveTodo.getId())
                .content(saveTodo.getContent())
                .completed(saveTodo.getCompleted())
                .createdAt(saveTodo.getCreatedAt())
                .updatedAt(saveTodo.getUpdatedAt())
                .build();
    }

    public TodoResponse complete(CustomUserDetails userDetails, Long todoId) {

        if(userDetails == null){
            throw new UnauthenticatedUserException("인증되지 않은 사용자입니다.");
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾지 못함"));

        TodoEntity todo = user.getTodo().stream()
                .filter(n -> n.getId() == todoId)
                .findFirst()
                .orElseThrow(() -> new TodoNotFoundException("할일을 찾을 수 없음"));

        if(todo.getCompleted()){
            todo.setCompleted(false);
        }else{
            todo.setCompleted(true);
        }

        todo.setUpdatedAt(LocalDateTime.now());

        TodoEntity save = todoEntityRepository.save(todo);

        return TodoResponse.builder()
                .id(save.getId())
                .content(save.getContent())
                .completed(save.getCompleted())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .build();
    }

    public UserResponse delete(CustomUserDetails customUserDetails, Long todoId) {

        if(customUserDetails == null){
            throw new UnauthenticatedUserException("인증되지 않은 사용자");
        }

        UserEntity user = userEntityRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없음"));

        TodoEntity todo = user.getTodo().stream()
                .filter(n -> n.getId() == todoId)
                .findFirst()
                .orElseThrow(() -> new TodoNotFoundException("할일을 찾을 수 없음"));

        user.getTodo().remove(todo);
        todoEntityRepository.delete(todo);

        List<TodoResponse> todoResponse = user.getTodo().stream()
                .map(value -> {
                    return TodoResponse.builder()
                            .id(value.getId())
                            .content(value.getContent())
                            .completed(value.getCompleted())
                            .createdAt(value.getCreatedAt())
                            .updatedAt(value.getUpdatedAt())
                            .build();
                }).collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole())
                .todo(todoResponse)
                .build();
    }

    public List<TodoResponse> list(CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new UnauthenticatedUserException("인증되지 않은 사용자");
        }

        UserEntity user = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾지 못함"));

        return user.getTodo().stream()
                .map(value -> {
                    return TodoResponse.builder()
                            .id(value.getId())
                            .content(value.getContent())
                            .completed(value.getCompleted())
                            .createdAt(value.getCreatedAt())
                            .updatedAt(value.getUpdatedAt())
                            .build();
                }).collect(Collectors.toList());
    }
}
