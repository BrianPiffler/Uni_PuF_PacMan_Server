package com.uni.pacmanserver.controller;

import java.util.Set;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.pacmanserver.entity.HistoryEntry;
import com.uni.pacmanserver.entity.User;
import com.uni.pacmanserver.repository.HistoryEntryRepository;
import com.uni.pacmanserver.repository.UserRepository;

@RestController
@RequestMapping("/api/history")
public class HistoryEntryController {

    @Autowired
    private HistoryEntryRepository historyEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllHistoryEntries(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Extrahieren des Principal-Objekts
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String uName = ((UserDetails) principal).getUsername();

            User idUser = userRepository.findUserByUsername(uName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (id == idUser.getId()) {
                Set<HistoryEntry> allHistoryEntriesInDb = historyEntryRepository.findAllByUserId(idUser.getId());
                return ResponseEntity.ok(allHistoryEntriesInDb);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong ID for authenticated user");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid authentication principal");
        }

    }

    @GetMapping("/best")
    public ResponseEntity<?> getBestEntry() {
        List<HistoryEntry> allEntries = historyEntryRepository.findAll();

        HistoryEntry bestEntry = null;
        for (HistoryEntry entry : allEntries) {
            if (bestEntry == null || entry.getGameScore() > bestEntry.getGameScore()) {
                bestEntry = entry;
            }
        }

        if (bestEntry != null) {
            return ResponseEntity.ok(bestEntry);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No entries found");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createHistoryEntry(@RequestBody HistoryEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Extrahieren des Principal-Objekts
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String uName = ((UserDetails) principal).getUsername();

            User idUser = userRepository.findUserByUsername(uName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            newEntry.setUserId(idUser.getId());

            HistoryEntry savedEntry = historyEntryRepository.save(newEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid authentication principal");
        }
    }
}
