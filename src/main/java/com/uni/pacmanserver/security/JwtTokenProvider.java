package com.uni.pacmanserver.security;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Value("${app.jwtSecret}")  // in application.properties gespeichert. Evtl. sicherer speichern!
    private String jwtSecret;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    // Erzeugt einen SecretKey basierend auf dem Base64-codierten jwtSecret
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret); // Base64-decodiertes Secret
        return Keys.hmacShaKeyFor(keyBytes); // SecretKey mit HMAC erzeugen
    }

    public String generateToken(String username) {
        Calendar cal = Calendar.getInstance();
        Date iat = cal.getTime(); // issued at
        cal.add(Calendar.HOUR, 1); // 1 Stunde gültig
        Date exp = cal.getTime(); // expiration time
    
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(iat)
            .setExpiration(exp)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateToken(user.getUsername());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey()) // Parser mit sicherem Key initialisieren
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        // Blacklist-Prüfung
        if (tokenBlacklist.contains(token)) {
            log.error("Blacklisted Token");
            return false;
        }

        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
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
