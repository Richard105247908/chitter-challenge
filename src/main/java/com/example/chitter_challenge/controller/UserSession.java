package com.example.chitter_challenge.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class UserSession {

    public static final String SESSION_USERNAME = "username";

    private UserSession() {
    }

    public static void logIn(HttpServletRequest request, HttpSession session, String username) {
        request.changeSessionId();
        session.setAttribute(SESSION_USERNAME, username);
    }

    public static String currentUsername(HttpServletRequest request) {
        return currentUsername(request.getSession(false));
    }

    public static String currentUsername(HttpSession session) {
        return session == null ? null : (String) session.getAttribute(SESSION_USERNAME);
    }
}