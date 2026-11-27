package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.example.chitter_challenge.controller.UserSession.SESSION_USERNAME;

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
                         HttpServletRequest request,
                         HttpSession session,
                         Model model) {
        Optional<User> maybeUser = userService.authenticate(username, password);
        if (maybeUser.isEmpty()) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("username", username);
            return "sessions/new";
        }

        request.changeSessionId();
        UserSession.logIn(request, session, maybeUser.get().getUsername());
        return "redirect:/peeps";
    }

    @PostMapping("/sessions/delete")
    public String delete(HttpSession session) {
        session.invalidate();
        return "redirect:/peeps";
    }




}
