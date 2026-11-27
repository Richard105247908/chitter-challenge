package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/new")
    public String newUser() {
        return "users/new";
    }
}
