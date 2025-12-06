package com.angelmorando.template.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**","/health","/v3/api-docs/**","/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            )
            // configure as a resource server for JWTs; customize jwk-set-uri in properties
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}
