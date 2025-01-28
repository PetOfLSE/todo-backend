package com.example.todo.auth.service;

import com.example.todo.auth.controller.request.RegisterRequest;
import com.example.todo.auth.controller.response.UserResponse;
import com.example.todo.auth.persistence.entity.UserEntity;
import com.example.todo.auth.persistence.repository.UserEntityRepository;
import com.example.todo.common.exception.AlreadyEmailRegisterException;
import com.example.todo.common.exception.AlreadyNicknameRegisterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userEntityRepository;

    public UserResponse register(RegisterRequest registerRequest) {
        if(userEntityRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyEmailRegisterException("이미 가입되어있는 이메일입니다.");
        }

        if(userEntityRepository.existsByNickname(registerRequest.getNickname())) {
            throw new AlreadyNicknameRegisterException("이미 가입되어있는 닉네임입니다.");
        }

        UserEntity entity = UserEntity.builder()
                .email(registerRequest.getEmail())
                .nickname(registerRequest.getNickname())
                .password(registerRequest.getPassword())
                .role("ROLE_USER")
                .build();

        userEntityRepository.save(entity);

        return UserResponse.builder()
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }
}
