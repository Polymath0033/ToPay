package com.polymath.topay.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.access}")
    private long accessTokenExpiration;

    @Value("${jwt.expiration.refresh}")
    private long refreshTokenExpiration;


    public String extractEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(String subject, Long teamId, UUID merchantId,String role) {
        return generateToken(subject, teamId, merchantId,role,accessTokenExpiration);
    }
    public String generateRefreshToken(String subject, Long teamId, UUID merchantId,String role) {
        return generateToken(subject, teamId, merchantId,role,refreshTokenExpiration);
    }

    private String generateToken(String subject, Long teamId, UUID merchantId,String role,long expiration) {
        Map<String, Object> extractClaims = new HashMap<>();
        extractClaims.put("teamId", teamId);
        extractClaims.put("merchantId", merchantId);
        extractClaims.put("role", role);
        return generateToken(extractClaims, subject,expiration);
    }

    public String generateToken(Map<String, Object> extraClaims, String subject,long expiration) {
        return buildToken(extraClaims, subject, expiration);
    }

    private  String buildToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .and()
                .signWith(getSecretKey()).compact();

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        final String email = extractEmailFromToken(token);
        return email != null && !isTokenExpired(token);
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public LocalDateTime extractExpirationDate(String token){
        return convertDateToLocalDateTime(extractExpiration(token));
    }

    private LocalDateTime convertDateToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}
