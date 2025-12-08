package com.angelmorando.template.service;

import com.angelmorando.template.service.dto.AuthResponse;
import com.angelmorando.template.service.dto.LoginRequest;
import com.angelmorando.template.service.dto.RegisterRequest;
import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAuthDao dao;
    private final PasswordEncoder passwordEncoder;
    private final com.angelmorando.template.security.auth.TokenService tokenService;

    @Transactional
    public void register(com.angelmorando.template.domain.auth.User user) {
        var existing = dao.selectUserByUsername(user.getUsername());
        if (existing != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserRow row = UserRow.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .enabled(user.getEnabled() != null ? user.getEnabled() : true)
                .accountNonExpired(user.getAccountNonExpired() != null ? user.getAccountNonExpired() : true)
                .accountNonLocked(user.getAccountNonLocked() != null ? user.getAccountNonLocked() : true)
                .credentialsNonExpired(user.getCredentialsNonExpired() != null ? user.getCredentialsNonExpired() : true)
                .build();
        dao.insertUser(row);
        dao.insertAuthority(user.getUsername(), "ROLE_USER");
    }

    public AuthResponse login(String username, String password) {
        var u = dao.selectUserByUsername(username);
        if (u == null) throw new IllegalArgumentException("Invalid credentials");
        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        // Create token
        var token = tokenService.createToken(u.getUsername());
        Instant expiresAt = Instant.now().plusSeconds(tokenService.getExpirySeconds());
        return new AuthResponse(token, expiresAt, u.getUsername());
    }
}
