package com.example.todo.common.custom;

import com.example.todo.auth.persistence.entity.UserEntity;
import com.example.todo.auth.persistence.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("UserDetailsService username : {}", username);

        UserEntity entity = userEntityRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new CustomUserDetails(entity);
    }
}
