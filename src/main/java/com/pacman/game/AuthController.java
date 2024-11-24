package com.pacman.game.server;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Geheimer Schlüssel für JWT
    private static final String SECRET_KEY = "secret"; // Achtung- sicher speicher!!! 


    /**
     * POST /api/auth/token - Benutzeranmeldung und JWT-Generierung
     */
    @PostMapping("/token")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        // Validierung der Benutzerdaten
        if (userService.validateUser(authRequest.getUsername(), authRequest.getPassword())) {
            String token = generateJwtToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * JWT Token generieren
     */
    private String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Stunden
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    
}