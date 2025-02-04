package com.example.todo.auth.service;

import com.example.todo.auth.controller.request.LoginRequest;
import com.example.todo.auth.controller.request.RefreshRequest;
import com.example.todo.auth.controller.request.RegisterRequest;
import com.example.todo.auth.controller.response.LoginResponse;
import com.example.todo.auth.controller.response.UserResponse;
import com.example.todo.auth.persistence.entity.RefreshTokenEntity;
import com.example.todo.auth.persistence.entity.UserEntity;
import com.example.todo.auth.persistence.repository.RefreshTokenEntityRepository;
import com.example.todo.auth.persistence.repository.UserEntityRepository;
import com.example.todo.common.exception.*;
import com.example.todo.common.jwt.JwtResponse;
import com.example.todo.common.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

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
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role("ROLE_USER")
                .todo(null)
                .build();

        userEntityRepository.save(entity);

        return UserResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .role(entity.getRole())
                .todo(null)
                .build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        JwtResponse jwtResponse = jwtUtil.generateToken(authenticate);
        String refreshToken = jwtResponse.getRefreshToken();

        Claims claims = jwtUtil.parseClaims(refreshToken);
        Long id = claims.get("id", Long.class);

        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없음"));

        Date expirationDate = claims.getExpiration();
        Date issuedAtDate = claims.getIssuedAt();

        LocalDateTime expiredAt = expirationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime issuedAt = issuedAtDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .expiresAt(expiredAt)
                .createdAt(issuedAt)
                .useYn("Y")
                .userSeq(user.getId())
                .build();

        if(refreshTokenEntityRepository.existsByUserSeq(refreshTokenEntity.getUserSeq())) {
            RefreshTokenEntity refresh = refreshTokenEntityRepository.findByUserSeq(refreshTokenEntity.getUserSeq())
                    .orElseThrow(() -> new TokenNotFoundException("찾을 수 없는 토큰 정보"));

            refreshTokenEntityRepository.delete(refresh);
        }

        refreshTokenEntityRepository.save(refreshTokenEntity);

        return LoginResponse.builder()
                .jwtResponse(jwtResponse)
                .nickname(user.getNickname())
                .build();
    }

    public LoginResponse refresh(RefreshRequest refreshRequest) {
        Claims claims = jwtUtil.parseClaims(refreshRequest.getRefreshToken());

        Date expiration = claims.getExpiration();
        RefreshTokenEntity refreshTokenEntity = refreshTokenEntityRepository.findByToken(refreshRequest.getRefreshToken())
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 토큰"));

        if(refreshTokenEntity.getUseYn().equals("N") || expiration.before(new Date())) {
            throw new ExpiredTokenException("사용이 만료된 토큰");
        }

        Authentication authentication = jwtUtil.getAuthentication(refreshRequest.getRefreshToken());
        JwtResponse jwtResponse = jwtUtil.generateToken(authentication);

        String refresh = jwtResponse.getRefreshToken();
        Claims refreshClaims = jwtUtil.parseClaims(refresh);

        Date expiredDate = refreshClaims.getExpiration();
        Date issuedDate = refreshClaims.getIssuedAt();

        LocalDateTime expired = expiredDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime issued = issuedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        String email = authentication.getName();
        UserEntity entity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾지 못함"));

        RefreshTokenEntity refreshEntity = RefreshTokenEntity.builder()
                .token(refresh)
                .expiresAt(expired)
                .createdAt(issued)
                .useYn("Y")
                .userSeq(entity.getId())
                .build();

        if(refreshTokenEntityRepository.existsByUserSeq(refreshTokenEntity.getUserSeq())) {
            refreshTokenEntityRepository.delete(refreshTokenEntity);
        }

        refreshTokenEntityRepository.save(refreshEntity);

        return LoginResponse.builder()
                .nickname(entity.getNickname())
                .jwtResponse(jwtResponse)
                .build();
    }
}
