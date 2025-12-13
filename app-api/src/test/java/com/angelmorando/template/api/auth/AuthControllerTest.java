package com.angelmorando.template.api.auth;

import com.angelmorando.template.service.AuthService;
import com.angelmorando.template.service.dto.AuthResponse;
import com.angelmorando.template.domain.auth.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = AuthController.class)
@org.springframework.context.annotation.Import(AuthController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @org.springframework.boot.SpringBootConfiguration
    static class TestConfig { }
    @Autowired
    MockMvc mvc;

    @MockBean
    AuthService authService;

    @Test
    void register_happyPath() throws Exception {
        Mockito.doNothing().when(authService).register(any(User.class));
        mvc.perform(post("/api/v1/register").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"u\", \"password\":\"password123\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void login_happyPath_setsCookie() throws Exception {
        var resp = new AuthResponse("token", Instant.now().plusSeconds(1800), "u");
        Mockito.when(authService.login(anyString(), anyString())).thenReturn(resp);
        mvc.perform(post("/api/v1/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"u\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("AUTH_TOKEN")))
                .andExpect(jsonPath("$.username", is("u")));
    }
}
