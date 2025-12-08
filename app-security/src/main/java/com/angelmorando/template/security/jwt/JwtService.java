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

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${app.security.auth.jwt.secret:}")
    private String jwtSecret;

    @Value("${app.security.auth.jwt.expiry-seconds:1800}")
    private long expirySeconds;

    private byte[] secretBytes;
    private OctetSequenceKey jwk;
    private JWSSigner signer;

    @PostConstruct
    public void init() throws Exception {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            // generate 256-bit random secret for dev
            jwtSecret = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        }
        secretBytes = jwtSecret.getBytes();
        jwk = new OctetSequenceKey.Builder(secretBytes).keyID(UUID.randomUUID().toString()).build();
        signer = new MACSigner(secretBytes);
    }

    public String createToken(String subject) {
        try {
            Instant now = Instant.now();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(subject)
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

    public long getExpirySeconds() { return expirySeconds; }
}
