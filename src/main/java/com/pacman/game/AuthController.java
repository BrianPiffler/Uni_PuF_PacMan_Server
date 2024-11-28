package com.pacman.game;

import java.util.Date;
import java.security.Key;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
     // Zugriff auf Benutzerdaten
    private UserRepository userRepository;
    @Autowired
     // Passwörter prüfen und verschlüsseln
    private PasswordEncoder passwordEncoder;

    // Geheimer Schlüssel für JWT
    private static final String SECRET_KEY = "secret"; // Achtung- sicher speicher!!! 


    /**
     * POST /api/auth/token - Benutzeranmeldung und JWT-Generierung
     * @param authRequest Enthält Benutzername und Passwort.
     * @return JWT-Token bei erfolgreicher Authentifizierung oder Fehlermeldung.
     */
    @PostMapping("/token")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        // Benutzer aus der Datenbank laden
        Optional<User> userOptional = userRepository.findByUsername(authRequest.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Passwort validieren
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                String token = generateJwtToken(user.getUsername());
                return ResponseEntity.ok(new AuthResponse(token));
            }
        }
        // Fehlermeldung, falls Benutzername oder Passwort ungültig sind
        return ResponseEntity.badRequest().body(null);
    }
    
    /**
     * JWT-Token generieren
     *
     * @param username Der Benutzername, der im Token gespeichert wird.
     * @return Ein signierter JWT-Token.
     */
    private String generateJwtToken(String username) {
        // Geheimen Schlüssel aus dem Base64-String erstellen
        byte[] secretKeyBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        
        // JWT-Token erstellen
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Stunden
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}