package com.prakash.productservice.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {
    public static  final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey";

     public String generateToken(String username,String role) {

         return Jwts.builder()
                 .subject(username)
                 .signWith(getSecretKey())
                 .claim("role",role) // Add roles or other claims as needed
                 .issuedAt(new Date())
                 .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                 .compact();
    }
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public Claims verifySignatureAndExtractClaims(String token) {
       return   Jwts.parser()
                 .verifyWith((SecretKey) getSecretKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload();
    }


     public boolean validateToken(String token) {
        // Implement JWT token validation logic here
        return !verifySignatureAndExtractClaims(token).getExpiration().before(new Date());
    }

     public String extractUsername(String token) {
        // Implement logic to extract username from JWT token here
        return verifySignatureAndExtractClaims(token).getSubject();
    }
}
