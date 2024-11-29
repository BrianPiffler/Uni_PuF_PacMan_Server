package com.uni.pacmanserver.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> userByName = userRepository.findUserByUsername(username);

        if (userByName.isPresent()) {
            return new ResponseEntity<User>(userByName.get(), HttpStatus.OK);
        }

        return new ResponseEntity("No User found by username" + username, HttpStatus.NOT_FOUND);
    }

    // TODO PatchMapping
}
