package com.example.todo.user.controller;

import com.example.todo.common.custom.CustomUserDetails;
import com.example.todo.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User API Controller")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/info")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> info(@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(userService.info(userDetails));
    }
}
