package com.example.phimmoi.utils;

import com.example.phimmoi.entity.InvalidatedToken;
import com.example.phimmoi.exception.AppException;
import com.example.phimmoi.exception.ErrorCode;
import com.example.phimmoi.repository.InvalidatedTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final String jwtSecret = "mySecretKey12345678901234567890123456789012345678901234567890";
    private long jwtExpirationMs = 20000; // milliseconds
    private long refreshExpirationMs = 86400000; // hours
    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;

    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String refreshToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = new Date(signedJWT.getJWTClaimsSet().getIssueTime().getTime() + refreshExpirationMs);

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String username = signedJWT.getJWTClaimsSet().getSubject();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .expiryDate(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
        return generateToken(username);
    }

    public void logout(String token) {
        var claims = getClaims(token, true);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(claims.getId())
                .expiryDate(claims.getExpiration())
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
    public Claims getClaims(String token, boolean isRefresh) {
        if(validateToken(token, isRefresh)) {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
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


    public boolean validateToken(String token, boolean isRefresh) {

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return !invalidatedTokenRepository.existsById(claims.getId());
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            long issueAt = claims.getIssuedAt().getTime();

            if (isRefresh) {
                return new Date().before(new Date(issueAt + refreshExpirationMs)) &&
                        !invalidatedTokenRepository.existsById(claims.getId());
            }
            System.out.println("Token hết hạn.");
            return false;

        } catch (SecurityException e) {
            System.out.println("Token không hợp lệ (security): " + e.getMessage());
            return false;

        } catch (JwtException e) {
            System.out.println("Token không hợp lệ (jwt): " + e.getMessage());
            return false;

        } catch (IllegalArgumentException e) {
            System.out.println("Token null hoặc rỗng: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.out.println("Chữ ký token không hợp lệ: " + e.getMessage());
            return false;
        }

    }
}

