package com.pacman.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST-Controller für Benutzeroperationen definieren
 * Bietet Endpunkte für die Registrierung und das Abrufen von Benutzerdaten.
 */
@RestController
@RequestMapping("/api/user") //Basis-URL für Endpunkte definieren
public class UserController {

    @Autowired
    // Repository für Datenbankoperationen
    private UserRepository userRepository; 
    @Autowired
     // Encoder für Passwörter
    private PasswordEncoder passwordEncoder;
    
    /**
     * Endpunkt zur Registrierung eines neuen Benutzers.
     * POST /api/user/register
     *
     * @param user Userdaten im JSON-Format übergeben werden.
     * @return Erfolgsmeldung oder Fehlermeldung.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Überprüfen, ob der Username bereits existiert
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Registration failed. Username may already exist.");
        }
        // User in der Datenbank speichern
        // Passwort verschlüsseln
        user.setPassword(passwordEncoder.encode(user.getPassword())); 
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

   /**
     * Endpunkt, um einen User anhand seiner ID abzurufen.
     * GET /api/user/<username>
     * 
     * @param id Die ID des Users.
     * @return Userdaten oder Fehlermeldung
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpunkt zur Aktualisierung eines Users. 
     * PATCH /api/user/<id>
     * @param id   Die ID des Benutzers, der aktualisiert werden soll.
     * @param updatedUser Die neuen Benutzerdaten.
     * @return Aktualisierte Benutzerdaten oder Fehlermeldung.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            // Zulässige Felder aktualisieren
            existingUser.setUsername(updatedUser.getUsername());
            // Passwort verschlüsseln
            existingUser.setGameScore(updatedUser.getGameScore());
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); 
            existingUser.setLevelReached(updatedUser.getLevelReached());
            existingUser.setDatePlayed(updatedUser.getDatePlayed());
            existingUser.setTimePlayed(updatedUser.getTimePlayed());
            userRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
