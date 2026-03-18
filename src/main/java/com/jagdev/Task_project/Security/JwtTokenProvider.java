package com.jagdev.Task_project.Security;

import com.jagdev.Task_project.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final long JWT_EXPIRATION_MS = 3600000;

    private static final String JWT_SECRET =
            "JWTSecretKeyJWTSecretKeyJWTSecretKeyJWTSecretKeyJWTSecretKeyJWTSecretKey";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication){
        String email=authentication.getName();
        Date currentDate=new Date();
        Date expiryDate=new Date(currentDate.getTime() + JWT_EXPIRATION_MS);
        String token= Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(),Jwts.SIG.HS512)
                .compact();
        return token;
    }

    public String getEmailFromToken(String token){
        Claims claims=Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token).getPayload();
       return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch(Exception e){
            throw new ApiException("Token issue:"+e.getMessage());
        }
    }
}