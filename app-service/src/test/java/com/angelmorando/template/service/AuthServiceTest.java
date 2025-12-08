package com.angelmorando.template.service;

import com.angelmorando.template.service.dto.AuthResponse;
import com.angelmorando.template.domain.auth.User;
import com.angelmorando.template.persistence.auth.dao.UserAuthDao;
import com.angelmorando.template.persistence.auth.model.UserRow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    private UserAuthDao dao;
    private PasswordEncoder passwordEncoder;
    private com.angelmorando.template.security.auth.TokenService tokenService;
    private AuthService service;

    @BeforeEach
    void setup() throws Exception {
        dao = Mockito.mock(UserAuthDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        tokenService = Mockito.mock(com.angelmorando.template.security.auth.TokenService.class);
        service = new AuthService(dao, passwordEncoder, tokenService);
    }

    @Test
    void register_whenUserExists_throws() {
        when(dao.selectUserByUsername("u")).thenReturn(new UserRow());
        // use domain User
        com.angelmorando.template.domain.auth.User user = com.angelmorando.template.domain.auth.User.builder().username("u").password("password123").build();
        assertThrows(IllegalArgumentException.class, () -> service.register(user));
    }

    @Test
    void register_success_insertsUser() {
        when(dao.selectUserByUsername(any())).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("hashed");
        com.angelmorando.template.domain.auth.User user = com.angelmorando.template.domain.auth.User.builder().username("u").password("password123").build();
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
        when(tokenService.createToken("u")).thenReturn("token");
        AuthResponse resp = service.login("u", "p");
        assertNotNull(resp);
        assertEquals("token", resp.getToken());
    }
}
