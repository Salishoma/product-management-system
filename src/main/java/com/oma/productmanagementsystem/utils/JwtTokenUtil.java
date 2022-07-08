package com.oma.productmanagementsystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
public class JwtTokenUtil implements Serializable
{

    private final Environment environment;

    private final SecretKey secretKey;

    @Autowired
    public JwtTokenUtil(Environment environment, SecretKey secretKey) {
        this.environment = environment;
        this.secretKey = secretKey;
    }

    // retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieving any information from token we will need the secret key
    @Deprecated
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();
    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // generate token for user
    public String generateToken(UserDetails userDetails, Authentication authentication) {
        String username = userDetails.getUsername();
        return doGenerateToken(authentication, username);
    }

    private String doGenerateToken(Authentication authentication, String subject) {
        return Jwts.builder().claim("authorities", authentication.getAuthorities())
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(requireNonNull(environment.getProperty("token.expiration_time")))))
                .signWith(secretKey, SignatureAlgorithm.HS512).compact();
    }

    // validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}