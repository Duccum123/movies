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
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final String jwtSecret = "mySecretKey12345678901234567890123456789012345678901234567890";
    private long jwtExpirationMs = 7200000; // milliseconds, 2h
    private long refreshExpirationMs = 86400000; // milliseconds, 1 day
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

        var signedJWT = getClaims(token, true);
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .expiryDate(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
        return generateToken(signedJWT.getJWTClaimsSet().getSubject());
    }

    public boolean validateToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(verifier);

        boolean isExpired = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().getTime() + refreshExpirationMs).before(new Date())
                : signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
        boolean isInBlackList = invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());
        return verified && !isExpired && !isInBlackList;

    }

    public void logout(String token) throws ParseException, JOSEException {
        var signedJWT = getClaims(token, true);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .expiryDate(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
    public SignedJWT getClaims(String token, boolean isRefresh) throws ParseException, JOSEException {
        if(validateToken(token, isRefresh)) {
            JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            if(!signedJWT.verify(verifier)) throw new AppException(ErrorCode.UNAUTHENTICATED);
            return signedJWT;
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
}