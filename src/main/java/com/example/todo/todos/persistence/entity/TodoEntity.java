package com.example.todo.todos.persistence.entity;

import com.example.todo.auth.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_seq")
    @Comment("할일 시퀀스")
    private Long id;

    @Column(name = "content", length = 300, nullable = false)
    @Comment("할일")
    private String content;

    @Column(name = "completed")
    @Comment("완료 여부")
    private boolean completed;

    @Column(name = "created_at")
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Comment("수정 일시")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    @Comment("사용자 시퀀스")
    private UserEntity user;
}
