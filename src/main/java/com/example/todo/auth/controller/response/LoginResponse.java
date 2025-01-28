package com.example.todo.auth.controller.response;

import com.example.todo.common.jwt.JwtResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String nickname;

    private JwtResponse jwtResponse;
}
