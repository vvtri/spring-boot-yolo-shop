package com.security.property;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@SpringBootConfiguration
@Getter
public class JwtProperties {
    @Value("${auth.jwt.secret-key}")
    private String jwtKey;

    @Value("${auth.jwt.expires-in}")
    private String expiresIn;

    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }

    public Long getExpiresIn() {
        return Long.parseLong(this.expiresIn)   ;
    }
}
