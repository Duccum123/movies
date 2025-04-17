package com.example.phimmoi.utils;

import com.example.phimmoi.entity.InvalidatedToken;
import com.example.phimmoi.exception.AppException;
import com.example.phimmoi.exception.ErrorCode;
import com.example.phimmoi.repository.InvalidatedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final String jwtSecret = "asasasasasasassaasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasaas";
    private final long jwtExpirationMs = 86400000; // 1 ngày
    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;

    public String generateToken(Authentication auth) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(auth.getName())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }
    public void logout(String token) {
        var claims = getClaims(token);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(claims.getId())
                .expiryDate(claims.getExpiration())
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            // kiểm tra blacklist
            return !invalidatedTokenRepository.existsById(claimsJws.getBody().getId());
        } catch (ExpiredJwtException | JwtException | IllegalArgumentException e) {
            return false;
        }

    }
}

