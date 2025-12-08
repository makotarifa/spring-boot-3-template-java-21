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
    private final com.angelmorando.template.service.JwtService jwtService;

    @Transactional
    public void register(RegisterRequest request) {
        var existing = dao.selectUserByUsername(request.getUsername());
        if (existing != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserRow row = UserRow.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
        dao.insertUser(row);
        dao.insertAuthority(request.getUsername(), "ROLE_USER");
    }

    public AuthResponse login(LoginRequest request) {
        var u = dao.selectUserByUsername(request.getUsername());
        if (u == null) throw new IllegalArgumentException("Invalid credentials");
        if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        // Create token
        var token = jwtService.createToken(u.getUsername());
        Instant expiresAt = Instant.now().plusSeconds(jwtService.getExpirySeconds());
        return new AuthResponse(token, expiresAt, u.getUsername());
    }
}
