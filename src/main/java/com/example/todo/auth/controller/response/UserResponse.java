package com.example.todo.auth.controller.response;

import com.example.todo.todos.controller.response.TodoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String email;

    private String nickname;

    private String role;

    private List<TodoResponse> todo;
}
