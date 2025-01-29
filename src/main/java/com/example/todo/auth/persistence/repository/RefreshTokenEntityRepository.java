package com.example.todo.auth.persistence.repository;

import com.example.todo.auth.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {

    boolean existsByUserSeq(Long userSeq);

    Optional<RefreshTokenEntity> findByUserSeq(Long userSeq);

    Optional<RefreshTokenEntity> findByToken(String token);
}
