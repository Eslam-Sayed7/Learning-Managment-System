//package com.Java.LMS.platform.service;
//
//import com.Java.LMS.platform.config.Security.JwtConfig;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JWTService {
//
//    private final JwtConfig jwtConfig;
//    private final Key signingKey;
//
//    public JWTService(JwtConfig jwtConfig) {
//        this.jwtConfig = jwtConfig;
//        this.signingKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
//    }
//
//    // Generate a new JWT for the specified username
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuer(jwtConfig.getIssuer())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
//                .signWith(signingKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Extract the username from the JWT
//    public String extractUsername(String token) {
//        return getClaims(token).getSubject();
//    }
//
//    // Validate the token for a specific username
//    public Boolean validateToken(String token, String username) {
//        return username.equals(extractUsername(token)) && !isTokenExpired(token);
//    }
//
//    // Check if the token is expired
//    private Boolean isTokenExpired(String token) {
//        return getClaims(token).getExpiration().before(new Date());
//    }
//
//    // Retrieve claims from the token
//    private Claims getClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(signingKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
