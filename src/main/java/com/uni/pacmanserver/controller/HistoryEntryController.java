package com.uni.pacmanserver.controller;

import java.util.Optional;
import java.util.Set;

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
    public ResponseEntity<Set<HistoryEntry>> getAllHistoryEntries(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }

        // Extrahieren des Principal-Objekts
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String uName = ((UserDetails) principal).getUsername();

            User idUser = userRepository.findUserByUsername(uName)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if(id == idUser.getId()) {
                Set<HistoryEntry> allHistoryEntriesInDb = historyEntryRepository.findAllByUserId(idUser.getId());
                return new ResponseEntity<Set<HistoryEntry>>(allHistoryEntriesInDb, HttpStatus.OK);
            } else {
                return new ResponseEntity("Wrong id for authenticated user", HttpStatus.FORBIDDEN);
            }
        }else {
            return new ResponseEntity("Invalid authentication principal", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("")
    public ResponseEntity<HistoryEntry> createHistoryEntry(@RequestBody HistoryEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }

        // Extrahieren des Principal-Objekts
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String uName = ((UserDetails) principal).getUsername();

            User idUser = userRepository.findUserByUsername(uName)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            newEntry.setUserId(idUser.getId());

            HistoryEntry savedEntry = historyEntryRepository.save(newEntry);
            return new ResponseEntity<HistoryEntry>(savedEntry, HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Invalid authentication principal", HttpStatus.FORBIDDEN);
        }
    }
}