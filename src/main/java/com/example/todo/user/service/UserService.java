package com.example.todo.user.service;

import com.example.todo.auth.controller.response.UserResponse;
import com.example.todo.auth.persistence.entity.UserEntity;
import com.example.todo.auth.persistence.repository.UserEntityRepository;
import com.example.todo.common.custom.CustomUserDetails;
import com.example.todo.common.exception.UnauthenticatedUserException;
import com.example.todo.common.exception.UserNotFoundException;
import com.example.todo.todos.controller.response.TodoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public UserResponse info(CustomUserDetails userDetails) {

        if(userDetails == null){
            throw new UnauthenticatedUserException("인증되지 않은 사용자");
        }

        UserEntity entity = userEntityRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없음"));

        List<TodoResponse> todo = entity.getTodo().stream()
                .map(value -> {
                    return TodoResponse.builder()
                            .id(value.getId())
                            .content(value.getContent())
                            .completed(value.getCompleted())
                            .createdAt(value.getCreatedAt())
                            .updatedAt(value.getUpdatedAt())
                            .build();
                })
                .toList();

        return UserResponse.builder()
                .id(entity.getId())
                .role(entity.getRole())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .todo(todo)
                .build();
    }
}
