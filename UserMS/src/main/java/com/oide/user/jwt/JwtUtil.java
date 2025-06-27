package com.oide.user.jwt;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60L * 1000;
    private static final String SECRET_KEY = "9f2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of(
                "email", ((CustomUserDetails) userDetails).getEmail(),
                "name",  ((CustomUserDetails) userDetails).getName()
        );
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return parse(token).getBody().getSubject();
    }

    public boolean validateToken(String token, UserDetails ud) {
        try {
            Jws<Claims> claims = parse(token);
            return claims.getBody().getSubject().equals(ud.getUsername())
                    && !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException|IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    }
}
