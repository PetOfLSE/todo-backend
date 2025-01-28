package com.example.todo.auth.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token_info")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_seq")
    @Comment("토큰 시퀀스")
    private Long id;

    @Column(name = "token", length = 1000, nullable = false, unique = true)
    @Comment("토큰")
    private String token;

    @Column(name = "expires_at")
    @Comment("만료 일시")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @Column(name = "use_yn", length = 10)
    @Comment("사용 여부")
    private String useYn;

    @Column(name = "user_seq")
    @Comment("사용자 시퀀스")
    private Long userSeq;
}
