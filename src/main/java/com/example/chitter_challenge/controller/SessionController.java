package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionController {

    private final UserService userService;


    public SessionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sessions/new")
    public String newSession() {
        return "sessions/new";
    }




}
