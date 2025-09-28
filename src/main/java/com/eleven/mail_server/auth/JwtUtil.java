package com.eleven.mail_server.auth;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtil {

    private static String accessToken;

    @Autowired
    private Environment env;

//  Get the signing key from the secret we created using
//  `openssl rand -base64 64`
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("email.server.secret"));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .claim("token_type","access")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey(),Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim("token_type", "refresh")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractClaim(String token, String claimKey) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(claimKey, String.class);
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenExpired(String token){
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return !expiration.before(new Date());
        } catch (Exception e) {
            return false; // Treat invalid tokens as expired
        }
    }

    public boolean isTokenValid(String token, String username,String expectedType){
        try {
            final String usernameFromToken = extractUsername(token);
            final String tokenType = extractClaim(token, "token_type");
            return (usernameFromToken.equals(username) &&
                    (Objects.equals(tokenType, expectedType))
                    && isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized String getValidAccessToken(String username, String refreshToken) {
        if (accessToken != null && isTokenExpired(accessToken)
                && isTokenValid(accessToken, username, "access")) {
            return accessToken;
        }
        // Use refresh token to generate new access token
        if (isTokenValid(refreshToken, username, "refresh")) {
            accessToken = generateToken(username);
            return accessToken;
        } else {
            throw new IllegalStateException("Refresh token invalid or expired. Please re-generate.");
        }
    }
}
