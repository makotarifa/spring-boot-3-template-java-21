package com.angelmorando.template.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;
import java.util.Optional;

@Configuration
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = false)
public class RegisteredClientDataLoader {

    @Bean
    public CommandLineRunner loadRegisteredClients(RegisteredClientRepository registeredClientRepository) {
        return args -> {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            // Client credentials client
            String clientId = "demo-client";
            if (registeredClientRepository.findByClientId(clientId) == null) {
                RegisteredClient clientCredentialsClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId(clientId)
                        .clientSecret(encoder.encode("demo-secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scope("read")
                        .scope("write")
                        .tokenSettings(org.springframework.security.oauth2.server.authorization.settings.TokenSettings.builder().build())
                        .build();
                registeredClientRepository.save(clientCredentialsClient);
            }

            // Authorization code client for OIDC-like flows
            String webClientId = "web-client";
            if (registeredClientRepository.findByClientId(webClientId) == null) {
                RegisteredClient webClient = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId(webClientId)
                        .clientSecret(encoder.encode("web-secret"))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://localhost:8080/login/oauth2/code/web-client")
                        .scope("openid")
                        .scope("profile")
                        .scope("read")
                        .clientName("Web Client Example")
                        .build();
                registeredClientRepository.save(webClient);
            }
        };
    }
}
