package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.DuplicateUserException;
import com.example.chitter_challenge.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/users")
    public String create(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String name,
                         @RequestParam String username,
                         HttpSession session,
                         Model model) {
        try {
            User saved = userService.signUp(email, password, name, username);
            session.setAttribute(PeepController.SESSION_USERNAME, saved.getUsername());
            return "redirect:/peeps";
        } catch (DuplicateUserException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            model.addAttribute("username", username);
            return "users/new";
        }
    }

}
