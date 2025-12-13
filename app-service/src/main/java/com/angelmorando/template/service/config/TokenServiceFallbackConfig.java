package com.angelmorando.template.service.config;

import com.angelmorando.template.security.auth.TokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "false", matchIfMissing = true)
public class TokenServiceFallbackConfig {
    @Bean
    public TokenService tokenService() {
        return new TokenService() {
            @Override
            public String createToken(String subject) {
                return "token-" + subject;
            }
            @Override
            public long getExpirySeconds() {
                return 1800;
            }
        };
    }
}

