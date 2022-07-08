package com.oma.productmanagementsystem.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.crypto.SecretKey;

import static java.util.Objects.requireNonNull;

@Configuration
public class JwtSecretKey {
    private final Environment environment;

    public JwtSecretKey(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SecretKey secretKey(){
        return Keys.hmacShaKeyFor(requireNonNull(environment.getProperty("token.secret")).getBytes());
    }
}