package com.pacman.game.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST-Controller für Benutzeroperationen definieren
 * Bietet Endpunkte für die Registrierung, Anmeldung und das Abrufen von Benutzerdaten.
 */
@RestController
@RequestMapping("/api/user") //Basis-URL für Endpunkte definieren
public class UserController {

    // Dependency Injection, Service-Schicht für die Geschäftslogik.
    @Autowired
    private UserService userService;
    
    /**
     * Endpunkt zur Registrierung eines neuen Benutzers.
     * POST /api/user/register
     *
     * @param user Userdaten im JSON-Format übergeben werden.
     * @return Erfolgsmeldung oder Fehlermeldung.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.registerUser(user)) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.badRequest().body("Registration failed. Username may already exist.");
        }
    }

    /**
     * Endpunkt zur Useranmeldung.
     *
     * @param username Username
     * @param password Passwort
     * @return Erfolgsmeldung oder Fehlermeldung.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        if (userService.validateUser(username, password)) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }
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
        Optional<User> user = userService.getUserByUsername(username);
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
        Optional<User> user = userService.updateUser(id, updatedUser);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
