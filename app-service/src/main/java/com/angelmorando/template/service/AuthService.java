package com.angelmorando.template.service;

import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.angelmorando.template.domain.auth.User;
import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;
import com.angelmorando.template.security.auth.TokenService;
import com.angelmorando.template.mappers.auth.UserMapper;
import com.angelmorando.template.service.dto.AuthResponse;
import com.angelmorando.template.exception.UserAlreadyExistsException;
import com.angelmorando.template.exception.InvalidCredentialsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAuthDao dao;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    @Transactional
    public void register(User user) {
        var existing = dao.selectUserByUsername(user.getUsername());
        if (existing != null) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        UserRow row = userMapper.toRow(user);
        // ensure password is stored hashed
        row.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.insertUser(row);
        dao.insertAuthority(user.getUsername(), "ROLE_USER");
    }

    public AuthResponse login(String username, String password) {
        var uRow = dao.selectUserByUsername(username);
        if (uRow == null) throw new InvalidCredentialsException("Invalid credentials");
        var u = userMapper.toDomain(uRow);
        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        // Create token
        var token = tokenService.createToken(u.getUsername());
        Instant expiresAt = Instant.now().plusSeconds(tokenService.getExpirySeconds());
        return new AuthResponse(token, expiresAt, u.getUsername());
    }
}
