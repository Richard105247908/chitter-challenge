package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.users = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(String email, String password, String name, String username) {
        String hashedPassword = passwordEncoder.encode(password);
        User passwordUser;
        passwordUser = new User(email, hashedPassword, name, username);
        users.save(passwordUser);
        return null;
    }

}
