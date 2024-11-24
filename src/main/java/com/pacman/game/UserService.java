package com.pacman.game.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service-Schicht für Benutzerverwaltung.
 * Verantwortlich für die Geschäftslogik und die Kommunikation mit dem Repository.
 */
@Service
public class UserService {

    // Repository für Datenbankoperationen.
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registriert einen neuen User
     * Überprüft, ob der Username bereits existiert.
     *
     * @param user Der User, der registriert werden soll.
     * @return true, wenn die Registrierung erfolgreich war, andernfalls false.
     */
    public boolean registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false; // Username existiert bereits
        }
        // User wird in der Datenbank gespeichert.
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Passwort verschlüsseln
        userRepository.save(user);
        return true;
    }

    /**
     * Überprüft die Anmeldedaten eines User.
     *
     * @param username Username
     * @param password Passwort.
     * @return true, wenn die Anmeldedaten korrekt sind, andernfalls false.
     */
    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    /**
     * Ruft einen User anhand seines Namens ab.
     *
     * @param username
     * @return Ein Optional mit dem User, falls gefunden.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }  


    /**
     * Userdaten aktualisieren
     */
    public Optional<User> updateUser(int id, User updatedUser) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
             // Nur die zulässigen Felder werden aktualisiert
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            //existingUser.setGameScore(updatedUser.getGameScore());
            //existingUser.setLevelReached(updatedUser.getLevelReached());
            //existingUser.setDatePlayed(updatedUser.getDatePlayed());
            //existingUser.setTimePlayed(updatedUser.getTimePlayed());
            userRepository.save(existingUser);
            return Optional.of(existingUser);
        }
        return Optional.empty();
    }

}
