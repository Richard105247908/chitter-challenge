package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.service.PeepService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PeepController {
    private final PeepService peepService;
    public static final String SESSION_USERNAME = "username";

    public PeepController(PeepService peepService) {
        this.peepService = peepService;
    }

    @GetMapping("/peeps")
    public String index(Model model) {
        model.addAttribute("peeps", peepService.getStream());
        return "peeps/index";
    }

    @GetMapping("/peeps/new")
    public String newPeepForm(HttpSession session) {
        if (session.getAttribute(SESSION_USERNAME) == null) {
            return "redirect:/sessions/new";
        }
        return "peeps/new";
    }
}
