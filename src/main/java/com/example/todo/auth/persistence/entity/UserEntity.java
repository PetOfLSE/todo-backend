package com.example.todo.auth.persistence.entity;

import com.example.todo.todos.persistence.entity.TodoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user_info", uniqueConstraints = {
        @UniqueConstraint(name = "email_uni", columnNames = "email"),
        @UniqueConstraint(name = "nickname_uni", columnNames = "nickname")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SEQ")
    @Comment("사용자 시퀀스")
    private Long id;

    @Column(name = "email", length = 200)
    @Comment("이메일")
    private String email;

    @Column(name = "nickname", length = 50)
    @Comment("닉네임")
    private String nickname;

    @Column(name = "password", length = 300)
    @Comment("비밀번호")
    private String password;

    @Column(name = "role", length = 10)
    @Comment("사용자 권한")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TodoEntity> todo = new ArrayList<>();
}
