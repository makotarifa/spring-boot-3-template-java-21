package com.angelmorando.template.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.angelmorando.template.domain.auth.User;
import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;
import com.angelmorando.template.service.dto.AuthResponse;

class AuthServiceTest {
    private UserAuthDao dao;
    private PasswordEncoder passwordEncoder;
    private com.angelmorando.template.security.auth.TokenService tokenService;
    private com.angelmorando.template.mappers.auth.UserMapper userMapper;
    private AuthService service;

    @BeforeEach
    void setup() throws Exception {
        dao = Mockito.mock(UserAuthDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        tokenService = Mockito.mock(com.angelmorando.template.security.auth.TokenService.class);
        userMapper = Mockito.mock(com.angelmorando.template.mappers.auth.UserMapper.class);
        service = new AuthService(dao, passwordEncoder, tokenService, userMapper);
    }

    @Test
    void register_whenUserExists_throws() {
        when(dao.selectUserByUsername("u")).thenReturn(new UserRow());
        // use domain User
        User user = User.builder().username("u").password("password123").build();
        assertThrows(IllegalArgumentException.class, () -> service.register(user));
    }

    @Test
    void register_success_insertsUser() {
        when(dao.selectUserByUsername(any())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        User user = User.builder().username("u").password("password123").build();
        when(userMapper.toRow(any(User.class))).thenReturn(UserRow.builder().username("u").password("password123").enabled(true).build());
        service.register(user);
        verify(dao, times(1)).insertUser(any());
        verify(dao, times(1)).insertAuthority(eq("u"), eq("ROLE_USER"));
    }

    @Test
    void login_whenInvalid_throws() {
        when(dao.selectUserByUsername("u")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> service.login("u", "p"));
    }

    @Test
    void login_success_returnsToken() {
        UserRow row = UserRow.builder().username("u").password("encoded").enabled(true).build();
        when(dao.selectUserByUsername("u")).thenReturn(row);
        when(passwordEncoder.matches("p", "encoded")).thenReturn(true);
        when(userMapper.toDomain(row)).thenReturn(User.builder().username("u").password("encoded").enabled(true).build());
        when(tokenService.createToken("u")).thenReturn("token");
        AuthResponse resp = service.login("u", "p");
        assertNotNull(resp);
        assertEquals("token", resp.getToken());
    }
}
