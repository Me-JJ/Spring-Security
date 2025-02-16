package com.Week5.SpringSecurity.services;

import com.Week5.SpringSecurity.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class JwtService
{
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey()
    {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(User user)
    {
        return Jwts.builder()
                .subject(user.getId().toString()) // user id in subject part of the token!
                .claim("email",user.getEmail())
                .claim("roles", Set.of("USER","ADMIN"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60))
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserIdFromToken(String token)
    {
        log.info("getUserdIdFromToken");
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        log.info("retuning ID from jwtService");
        return Long.valueOf(claims.getSubject()); // get the subject as above we stored the userId in subject of the token
    }
}
