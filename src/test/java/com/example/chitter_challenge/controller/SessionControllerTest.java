package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

import static com.example.chitter_challenge.controller.UserSession.SESSION_USERNAME;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Test
    void showsTheLoginForm() throws Exception {
        mockMvc.perform(get("/sessions/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("sessions/new"))
                .andExpect(content().string(containsString("name=\"username\"")))
                .andExpect(content().string(containsString("type=\"password\"")));
    }

    @Test
    void postSessionsLogsInUserAndRedirectsWhenCredentialsMatch() throws Exception {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex_saved");
        when(userService.authenticate("alex1", "password123"))
                .thenReturn(Optional.of(alex));

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/sessions")
                        .session(session)
                        .param("username", "alex1")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peeps"));

        verify(userService).authenticate("alex1", "password123");
        assertEquals("alex_saved", session.getAttribute(SESSION_USERNAME));
    }

    @Test
    void postSessionsRedisplaysFormWithErrorWhenCredentialsAreInvalid() throws Exception {
        when(userService.authenticate("alex1", "wrong-password"))
                .thenReturn(Optional.empty());

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/sessions")
                        .session(session)
                        .param("username", "alex1")
                        .param("password", "wrong-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("sessions/new"))
                .andExpect(model().attribute("error", "Invalid username or password"))
                .andExpect(model().attribute("username", "alex1"))
                .andExpect(model().attributeDoesNotExist("password"));

        verify(userService).authenticate("alex1", "wrong-password");
        assertNull(session.getAttribute(SESSION_USERNAME));
    }

    @Test
    void postSessionsRendersErrorAndUsernameWithoutPasswordInHtml() throws Exception {
        when(userService.authenticate("alex1", "wrong-password"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/sessions")
                        .param("username", "alex1")
                        .param("password", "wrong-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("sessions/new"))
                .andExpect(content().string(containsString("Invalid username or password")))
                .andExpect(content().string(containsString("alex1")))
                .andExpect(content().string(not(containsString("wrong-password"))));
    }

    @Test
    void loginChangesTheSessionId() throws Exception {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(userService.authenticate("alex1", "password123")).thenReturn(Optional.of(alex));

        MockHttpSession session = new MockHttpSession();
        String before = session.getId();

        MvcResult result = mockMvc.perform(post("/sessions")
                        .session(session)
                        .param("username", "alex1")
                        .param("password", "password123"))
                .andReturn();

        assertNotEquals(before, result.getRequest().getSession().getId());
    }

    @Test
    void postSessionsDeleteInvalidatesSessionAndRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(UserSession.SESSION_USERNAME, "alex1");

        mockMvc.perform(post("/sessions/delete").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peeps"));

        assertTrue(session.isInvalid());
    }

}