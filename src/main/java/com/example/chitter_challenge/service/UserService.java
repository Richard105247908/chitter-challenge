package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.users = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(String email, String password, String name, String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidUserException("Username must not be blank");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidUserException("Email is required");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidUserException("Name is required");
        }

        if (password == null || password.isBlank()) {
            throw new InvalidUserException("Password is required");
        }
        if (password.length() < 8) {
            throw new InvalidUserException("Password must be at least 8 characters");
        }

        if (users.findByUsername(username).isPresent()) {
            throw new DuplicateUserException("That username is already taken");
        }

        if (users.findByEmail(email).isPresent()) {
            throw new DuplicateUserException("That email is already taken");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(email, hashedPassword, name, username);
        try {
            return users.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException("That username or email is already in use");
        }
    }

    public Optional<User> findByUsername(String username) {

        return users.findByUsername(username);
    }

    public Optional<User> authenticate(String username, String password) {
        return users.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()));
    }

}
