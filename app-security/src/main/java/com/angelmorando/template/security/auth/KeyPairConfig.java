package com.angelmorando.template.security.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Configuration
public class KeyPairConfig {
    @Bean
    public KeyPair keyPair(
            @org.springframework.beans.factory.annotation.Value("${app.security.auth.rsa.private-key-pem:}") String privatePem,
            @org.springframework.beans.factory.annotation.Value("${app.security.auth.rsa.public-key-pem:}") String publicPem
    ) {
        try {
            if (privatePem != null && !privatePem.isBlank() && publicPem != null && !publicPem.isBlank()) {
                java.security.spec.PKCS8EncodedKeySpec privSpec = new java.security.spec.PKCS8EncodedKeySpec(
                        java.util.Base64.getDecoder().decode(sanitizePem(privatePem)));
                java.security.spec.X509EncodedKeySpec pubSpec = new java.security.spec.X509EncodedKeySpec(
                        java.util.Base64.getDecoder().decode(sanitizePem(publicPem)));
                java.security.KeyFactory kf = java.security.KeyFactory.getInstance("RSA");
                var privateKey = kf.generatePrivate(privSpec);
                var publicKey = kf.generatePublic(pubSpec);
                return new KeyPair((java.security.interfaces.RSAPublicKey) publicKey, (java.security.interfaces.RSAPrivateKey) privateKey);
            }
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            return gen.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String sanitizePem(String pem) {
        return pem.replace("-----BEGIN PRIVATE KEY-----", "")
                  .replace("-----END PRIVATE KEY-----", "")
                  .replace("-----BEGIN PUBLIC KEY-----", "")
                  .replace("-----END PUBLIC KEY-----", "")
                  .replaceAll("\r?\n", "").trim();
    }
}
