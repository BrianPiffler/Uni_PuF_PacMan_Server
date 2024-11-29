package com.uni.pacmanserver.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.pacmanserver.entity.User;
import com.uni.pacmanserver.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

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

    // TODO PatchMapping
}
