package com.angelmorando.template.security.auth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "app.security.enabled", havingValue = "true", matchIfMissing = false)
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "app.security.auth.algorithm", havingValue = "rs256")
@Service
public class RsaTokenService implements TokenService {
    private final KeyPair keyPair;

    @Value("${app.security.auth.jwt.expiry-seconds:1800}")
    private long expirySeconds;

    @Value("${app.security.auth.issuer:http://localhost:8080}")
    private String issuer;

    private JWSSigner signer;

    public RsaTokenService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @PostConstruct
    public void init() {
        signer = new RSASSASigner((RSAPrivateKey) keyPair.getPrivate());
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
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public long getExpirySeconds() { return expirySeconds; }
}
