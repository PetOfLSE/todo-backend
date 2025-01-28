package com.example.todo.auth.controller;

import com.example.todo.auth.controller.request.LoginRequest;
import com.example.todo.auth.controller.request.RegisterRequest;
import com.example.todo.auth.controller.response.LoginResponse;
import com.example.todo.auth.controller.response.UserResponse;
import com.example.todo.auth.service.AuthService;
import com.example.todo.common.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Tag(name = "Auth API Controller")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "사용자 회원가입 API", description = "사용자 회원가입을 처리하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }, description = "성공시 반환"),
            @ApiResponse(responseCode = "400", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "이미 가입된 이메일 또는 닉네임으로 재가입 요청을 보낼 경우 반환")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "사용자 회원가입 요청 객체",
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))

            )
            @RequestBody RegisterRequest registerRequest
    ){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @Operation(summary = "사용자 로그인 API", description = "사용자 로그인 처리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
            }, description = "로그인 성공 시 반환")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "로그인 요청 객체",
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody LoginRequest loginRequest
    ){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
