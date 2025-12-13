package com.angelmorando.template.security.auth;

public interface TokenService {
    String createToken(String subject);
    long getExpirySeconds();
}
