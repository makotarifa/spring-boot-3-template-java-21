package com.angelmorando.template.api.auth;

import com.angelmorando.template.service.AuthService;
import com.angelmorando.template.service.dto.AuthResponse;
import com.angelmorando.template.service.dto.LoginRequest;
import com.angelmorando.template.service.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    AuthService authService;

    @Test
    void register_happyPath() throws Exception {
        Mockito.doNothing().when(authService).register(any(RegisterRequest.class));
        mvc.perform(post("/1.0/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"u\", \"password\":\"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void login_happyPath_setsCookie() throws Exception {
        var resp = new AuthResponse("token", Instant.now().plusSeconds(1800), "u");
        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(resp);
        mvc.perform(post("/1.0/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"u\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("AUTH_TOKEN")))
                .andExpect(jsonPath("$.username", is("u")));
    }
}
