package com.teamtaskmanager.config;

import com.teamtaskmanager.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final Key signingKey;
  private final long expirationMillis;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expirationMinutes}") long expirationMinutes) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMillis = expirationMinutes * 60 * 1000;
  }

  public String generateToken(UserPrincipal principal) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMillis);
    return Jwts.builder()
        .setSubject(principal.getId().toString())
        .claim("email", principal.getUsername())
        .claim("name", principal.getName())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims parseClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
  }

  public String extractUserId(String token) {
    return parseClaims(token).getSubject();
  }
}
