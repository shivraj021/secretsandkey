package com.advanceauth.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expiration-ms}")
    private long expiration;

    public String generateToken(String subject) {
        return Jwts.builder()
          .setSubject(subject)
          .setIssuedAt(new Date())
          .setExpiration(new Date(System.currentTimeMillis() + expiration))
          .signWith(SignatureAlgorithm.HS256, secret)
          .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser().setSigningKey(secret)
          .parseClaimsJws(token).getBody().getSubject();
    }
}

