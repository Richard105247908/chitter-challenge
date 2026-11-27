package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/new")
    public String newUser() {
        return "users/new";
    }
}
