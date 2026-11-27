package com.example.chitter_challenge.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserAdvice {

    @ModelAttribute("currentUsername")
    public String currentUsername(HttpServletRequest request) {
        return UserSession.currentUsername(request);
    }
}