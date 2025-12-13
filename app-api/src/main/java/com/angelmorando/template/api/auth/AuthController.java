package com.angelmorando.template.api.auth;

import com.angelmorando.template.api.auth.dto.AuthResponse;
import com.angelmorando.template.api.auth.dto.LoginRequest;
import com.angelmorando.template.api.auth.dto.RegisterRequest;
import com.angelmorando.template.api.ControllerUtils;
import com.angelmorando.template.service.AuthService;
import com.angelmorando.template.domain.auth.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping(ControllerUtils.REGISTER)
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        authService.register(user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Login and receive auth token cookie")
    @PostMapping(ControllerUtils.LOGIN)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        com.angelmorando.template.service.dto.AuthResponse svcResp = authService.login(request.getUsername(), request.getPassword());
        ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", svcResp.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.between(java.time.Instant.now(), svcResp.getExpiresAt()).getSeconds())
                .build();
        AuthResponse resp = new AuthResponse(svcResp.getToken(), svcResp.getExpiresAt(), svcResp.getUsername());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resp);
    }
}
