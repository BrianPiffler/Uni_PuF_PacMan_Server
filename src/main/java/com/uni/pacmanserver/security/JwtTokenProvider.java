package com.uni.pacmanserver.security;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")  // in application.properties gespeichert. Evtl. sicherer speichern!
    private String jwtSecret;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateToken(String username) {
        Calendar cal = Calendar.getInstance();
        Date iat = cal.getTime(); // issued at
        cal.add(Calendar.HOUR, 1); // 1 Stunde gültig
        Date exp = cal.getTime(); // expiration time
    
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(iat)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateToken(user.getUsername());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts
            .parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
