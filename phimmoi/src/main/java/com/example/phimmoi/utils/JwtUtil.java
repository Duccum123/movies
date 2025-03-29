package com.example.phimmoi.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret";
    private final long EXPIRATION_TIME = 86400000; // 1 ngày (ms)

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Tạo JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Payload
                .setIssuedAt(new Date()) // Thời gian tạo
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Hết hạn
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Ký với thuật toán HS256
                .compact();
    }

    // Xác thực JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // Token không hợp lệ
        }
    }

    // Lấy username từ JWT
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
