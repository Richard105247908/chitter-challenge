package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.service.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
