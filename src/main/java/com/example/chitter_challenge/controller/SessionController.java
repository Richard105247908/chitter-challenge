package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/sessions")
    public String create(@RequestParam String username,
                         @RequestParam String password,
                         HttpSession session) {
        User user = userService.authenticate(username, password).get();
        session.setAttribute(PeepController.SESSION_USERNAME, user.getUsername());
        return "redirect:/peeps";
    }




}
