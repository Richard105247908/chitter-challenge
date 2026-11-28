package com.example.chitter_challenge.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    @Test
    void currentUsernameReturnsNullWhenSessionIsNull() {
        assertNull(UserSession.currentUsername((HttpSession) null));
    }

    @Test
    void logInRotatesSessionIdAndStoresUsername() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        String originalId = session.getId();

        UserSession.logIn(request, session, "alex1");

        assertNotEquals(originalId, session.getId());
        assertEquals("alex1", UserSession.currentUsername(session));
    }

    @Test
    void currentUsernameFromRequestDoesNotCreateSessionWhenNoneExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertNull(UserSession.currentUsername(request));
        assertNull(request.getSession(false));
    }

    @Test
    void currentUsernameFromRequestReturnsTheLoggedInUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(UserSession.SESSION_USERNAME, "alex1");
        request.setSession(session);

        assertEquals("alex1", UserSession.currentUsername(request));
    }
}