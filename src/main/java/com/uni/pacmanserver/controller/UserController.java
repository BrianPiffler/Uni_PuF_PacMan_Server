package com.uni.pacmanserver.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.pacmanserver.entity.User;
import com.uni.pacmanserver.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Endpunkt, um einen Userdaten anhand seines usernames abzurufen.
     * GET /api/user/<username>
     * 
     * @param username 
     * @return Userdaten oder Fehlermeldung
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
    //public ResponseEntity<User> getUser(@PathVariable String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
            //return new ResponseEntity("User is not authenticated", HttpStatus.UNAUTHORIZED);
        } else {

            // Extrahieren des Principal-Objekts
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String uName = ((UserDetails) principal).getUsername();
                if(username.equals(uName)) {
                    Optional<User> userByName = userRepository.findUserByUsername(username);

                    if (userByName.isPresent()) {
                        return new ResponseEntity<User>(userByName.get(), HttpStatus.OK);
                    } else {                
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No User found by username: " + username);
                        //return new ResponseEntity("No User found by username" + username, HttpStatus.NOT_FOUND);
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Wrong username for authenticated user");
                    //return new ResponseEntity("Wrong username for authenticated user", HttpStatus.FORBIDDEN);
                } 
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid authentication principal");
                //return new ResponseEntity("Invalid authentication principal", HttpStatus.FORBIDDEN);
            }
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
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String uName = ((UserDetails) principal).getUsername();
    
            // Sicherstellen, dass der Benutzer nur seine eigenen Daten aktualisiert
            Optional<User> optionalUser = userRepository.findUserByUsername(uName);
            if (optionalUser.isEmpty() || optionalUser.get().getId() != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this user");
            }
    
            User existingUser = optionalUser.get();
    
            // Nur die Felder aktualisieren, die übergeben wurden
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUser.getUsername());
            }
    
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }
    
            // Benutzer in der Datenbank speichern
            User savedUser = userRepository.save(existingUser);
    
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid authentication principal");
        }
    }
}
