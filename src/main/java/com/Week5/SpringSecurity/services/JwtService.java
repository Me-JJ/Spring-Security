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
    public String generateAccessToken(User user)
    {
        return Jwts.builder()
                .subject(user.getId().toString()) // user id in subject part of the token!
                .claim("email",user.getEmail())
                .claim("roles", user.getRoles().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10)) // 10 mins
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(User user)
    {
        return Jwts.builder()
                .subject(user.getId().toString()) // user id in subject part of the token!
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L*60*60*24*30*6)) // 6 months
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserIdFromToken(String token)
    {
        log.info("get UserdId From Token");
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        log.info("retuning ID from jwtService");
        return Long.valueOf(claims.getSubject()); // get the subject as above we stored the userId in subject of the token
    }
}
