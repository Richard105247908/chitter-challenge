package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.InvalidPeepException;
import com.example.chitter_challenge.service.PeepService;
import com.example.chitter_challenge.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.example.chitter_challenge.controller.UserSession.SESSION_USERNAME;

@Controller
public class PeepController {
    private final PeepService peepService;
    private final UserService userService;

    public PeepController(PeepService peepService, UserService userService) {
        this.peepService = peepService;
        this.userService = userService;
    }

    @GetMapping("/peeps")
    public String index(Model model) {
        model.addAttribute("peeps", peepService.getStream());
        return "peeps/index";
    }

    @GetMapping("/peeps/new")
    public String newPeepForm(HttpSession session) {
        String username = UserSession.currentUsername(session);
        if (username == null) {
            return "redirect:/sessions/new";
        }
        return "peeps/new";
    }

    @PostMapping("/peeps")
    public String create(@RequestParam String message, HttpSession session, Model model) {
        String username = UserSession.currentUsername(session);
        if (username == null) {
            return "redirect:/sessions/new";
        }

        Optional<User> maybeUser = userService.findByUsername(username);
        if (maybeUser.isEmpty()) {
            session.invalidate();
            return "redirect:/sessions/new";
        }

        try {
            peepService.post(maybeUser.get(), message);
        } catch (InvalidPeepException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("message", message);
            return "peeps/new";
        }

        return "redirect:/peeps";
    }
}
