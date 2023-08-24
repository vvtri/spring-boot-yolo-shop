package com.security.util;

import com.security.property.JwtProperties;
import com.yoloshop.enumeration.ClaimKey;
import com.yoloshop.enumeration.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    JwtProperties jwtProperties;


    String createToken(String subject, Role[] roles) {
        Date expiration = Date.from(Instant.now().plusSeconds(jwtProperties.getExpiresIn()));
        Claims claims = Jwts.claims();
        claims.put(ClaimKey.USER_ROLES.name(), roles);

        return Jwts.builder().setSubject(subject).signWith(jwtProperties.getSecretKey())
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .setClaims(claims).compact();
    }

    public Claims validateAndParseToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(jwtProperties.getSecretKey())
                    .build().parseClaimsJws(token).getBody();
        } catch (JwtException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token is not valid", exception);
        }
    }
}
