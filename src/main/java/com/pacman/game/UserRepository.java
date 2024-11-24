package com.pacman.game.server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository für Userentities
 * Stellt Datenbankzugriffe für User bereit.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Sucht einen User anhand seines Namens.
     *
     * @param username Username.
     * @return Ein Optional mit dem User, falls gefunden.
     */
    Optional<User> findByUsername(String username);
}