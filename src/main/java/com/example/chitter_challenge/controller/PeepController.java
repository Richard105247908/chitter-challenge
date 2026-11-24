package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.service.PeepService;
import org.springframework.stereotype.Controller;

@Controller
public class PeepController {
    private final PeepService peepService;

    public PeepController(PeepService peepService) {
        this.peepService = peepService;
    }
}
