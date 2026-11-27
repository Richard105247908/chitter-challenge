package com.example.chitter_challenge.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

class UserSessionTest {

    @Test
    void currentUsernameReturnsNullWhenSessionIsNull() {
        assertNull(UserSession.currentUsername((MockHttpSession) null));
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
}