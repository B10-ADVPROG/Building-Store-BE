package id.ac.ui.cs.advprog.buildingstore.authentication.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import java.util.Date;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private String secretKey = "Y29uZ3JhdHVsYXRpb25zdHJvbmdwZXJzZXZlbnRvcm5m5ybW8aGVg=";

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}