package com.aijw.cuidarplus.security;

import com.aijw.cuidarplus.model.TipoUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final String CLAIM_TIPO_USUARIO = "tipoUsuario";

    private final JwtProperties properties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = buildKey(properties.getSecret());
    }

    public String generateToken(Long id, String email, TipoUsuario tipoUsuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put(CLAIM_TIPO_USUARIO, tipoUsuario.name());

        Instant now = Instant.now();
        Instant expiration = now.plusMillis(properties.getExpiration());

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuer(properties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(properties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public TipoUsuario extractTipoUsuario(String token) {
        String tipoUsuario = parseClaims(token).get(CLAIM_TIPO_USUARIO, String.class);
        return TipoUsuario.valueOf(tipoUsuario);
    }

    public Long extractId(String token) {
        Number id = parseClaims(token).get("id", Number.class);
        return id == null ? null : id.longValue();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getSubject() != null && claims.getExpiration() != null && claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    private SecretKey buildKey(String secret) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

