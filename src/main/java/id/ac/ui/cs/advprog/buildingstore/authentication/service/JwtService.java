package id.ac.ui.cs.advprog.buildingstore.authentication.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final Set<String> invalidatedTokens = new HashSet<>();

    public JwtService() {
        byte[] keyBytes = "Y29uZ3JhdHVsYXRpb25zdHJvbmdwZXJzZXZlbnRvcm5m5ybW8aGVg=".getBytes();
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("fullname", user.getFullname())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("email", String.class);
    }

    public String extractFullname(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("fullname", String.class);
    }


    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            synchronized(invalidatedTokens) {
                return !invalidatedTokens.contains(token) &&
                        claims.getExpiration().after(new Date());
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void invalidateToken(String token) {
        synchronized(invalidatedTokens) {
            invalidatedTokens.add(token);
        }
    }
}