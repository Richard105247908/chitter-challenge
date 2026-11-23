package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public User signUp(String email, String passwordEncoder,String name, String username){
        return null;
    }
}
