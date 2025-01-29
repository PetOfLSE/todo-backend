package com.example.todo.todos.controller;

import com.example.todo.auth.controller.response.UserResponse;
import com.example.todo.common.custom.CustomUserDetails;
import com.example.todo.common.exception.response.ExceptionResponse;
import com.example.todo.todos.controller.request.TodoAddRequest;
import com.example.todo.todos.controller.response.TodoResponse;
import com.example.todo.todos.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
@Tag(name = "할일 API Controller")
public class TodoController {

    private final TodoService service;

    @Operation(summary = "할일 추가 API", description = "할일 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "사용자를 찾을 수 없는 경우 반환")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add")
    public ResponseEntity<TodoResponse> add(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "할일 추가 요청 객체",
                    content = @Content(schema = @Schema(implementation = TodoAddRequest.class))
            )
            @RequestBody TodoAddRequest request
            )
    {
        return ResponseEntity.ok(service.add(userDetails, request));
    }

    @Operation(summary = "할일 완료 API", description = "할일을 완료 상태로 변경하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "사용자 또는 토큰을 찾지 못할 경우 반환")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/complete")
    public ResponseEntity<TodoResponse> complete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(name = "todoId", required = true, description = "할일 시퀀스")
            @RequestParam(name = "todoId") Long todoId
            ){
        return ResponseEntity.ok(service.complete(userDetails, todoId));
    }

    @Operation(summary = "할일 삭제 API", description = "할일 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            }, description = "성공 시 반환"),
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            }, description = "인증되지 않은 사용자일 경우 반환"),
            @ApiResponse(responseCode = "404", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete")
    public ResponseEntity<UserResponse> delete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Parameter(name = "todoId", required = true, description = "할일 시퀀스")
            @RequestParam(name = "todoId") Long todoId
    ){
        return ResponseEntity.ok(service.delete(customUserDetails, todoId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/list")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TodoResponse>> list(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(service.list(userDetails));
    }

}
