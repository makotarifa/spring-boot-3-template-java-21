package com.angelmorando.template.security.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = false)
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "app.security.auth.algorithm", havingValue = "hs256", matchIfMissing = true)
@Service("securityJwtService")
public class JwtService implements com.angelmorando.template.security.auth.TokenService {
    @Value("${app.security.auth.jwt.secret:}")
    private String jwtSecret;

    @Value("${app.security.auth.jwt.expiry-seconds:1800}")
    private long expirySeconds;

    @Value("${app.security.auth.issuer:http://localhost:8080}")
    private String issuer;

    private byte[] secretBytes;
    private OctetSequenceKey jwk;
    private JWSSigner signer;

    @PostConstruct
    public void init() throws Exception {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("JWT secret is not set. Configure 'app.security.auth.jwt.secret'.");
        }
        secretBytes = jwtSecret.getBytes();
        jwk = new OctetSequenceKey.Builder(secretBytes).keyID(UUID.randomUUID().toString()).build();
        signer = new MACSigner(secretBytes);
    }

    @Override
    public String createToken(String subject) {
        try {
            Instant now = Instant.now();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer(issuer)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(expirySeconds)))
                    .claim("scope", "ROLE_USER")
                    .jwtID(UUID.randomUUID().toString())
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public long getExpirySeconds() { return expirySeconds; }
}
