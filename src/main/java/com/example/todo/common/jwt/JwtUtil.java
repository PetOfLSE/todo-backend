package com.example.todo.common.jwt;

import com.example.todo.auth.persistence.entity.UserEntity;
import com.example.todo.auth.persistence.repository.UserEntityRepository;
import com.example.todo.common.custom.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    private final Key key;
    private final UserEntityRepository userEntityRepository;

    public JwtUtil(@Value("${jwt.secret}") String value, UserEntityRepository userEntityRepository) {
        byte[] bytes = Decoders.BASE64.decode(value);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.userEntityRepository = userEntityRepository;
    }

    public JwtResponse generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        long nowLong = now.getTime();

        String email = authentication.getName();
        UserEntity entity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Date accessExpired = new Date(nowLong + 86400000);
        String accessToken = Jwts.builder()
                .setSubject("access")
                .claim("auth", authorities)
                .claim("id", entity.getId())
                .setExpiration(accessExpired)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshExpired = new Date(nowLong + 86400000);
        String refreshToken = Jwts.builder()
                .setSubject("refresh")
                .claim("auth", authorities)
                .claim("id", entity.getId())
                .setExpiration(refreshExpired)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtResponse.builder()
                .type("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessExpires(accessExpired.getTime())
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if(claims.get("auth") == null){
            throw new RuntimeException("권한이 없음");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String strId = claims.get("id").toString();
        Long id = Long.valueOf(strId);

        UserEntity entity = userEntityRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails customUserDetails = new CustomUserDetails(entity);
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public Claims parseClaims(String token) {
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
