package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.service.PeepService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PeepController {
    private final PeepService peepService;

    public PeepController(PeepService peepService) {
        this.peepService = peepService;
    }

    @GetMapping("/peeps")
    public String index(Model model) {
        model.addAttribute("peeps", peepService.getStream());
        return "peeps/index";
    }
}
