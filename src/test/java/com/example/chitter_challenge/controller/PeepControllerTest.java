package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.Peep;
import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.InvalidPeepException;
import com.example.chitter_challenge.service.PeepService;
import com.example.chitter_challenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.chitter_challenge.controller.UserSession.SESSION_USERNAME;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PeepController.class)
class PeepControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PeepService peepService;

    @MockitoBean
    UserService userService;

    @Test
    void showsThePeepStreamWithAuthorAndMessage() throws Exception {
        Peep newer = new Peep(2L, "Second peep", OffsetDateTime.parse("2026-11-19T11:00:00Z"), "Alex", "alex1");
        Peep older = new Peep(1L, "First peep", OffsetDateTime.parse("2026-11-19T10:00:00Z"), "Alex", "alex1");
        when(peepService.getStream()).thenReturn(List.of(newer, older));

        mockMvc.perform(get("/peeps"))
                .andExpect(status().isOk())
                .andExpect(view().name("peeps/index"))
                .andExpect(model().attribute("peeps", List.of(newer, older)))
                .andExpect(content().string(containsString("Alex (@alex1)")))
                .andExpect(content().string(containsString("Second peep")));
    }

    @Test
    void newPeepFormRedirectsToLoginWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/peeps/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions/new"));
    }


    @Test
    void showsNewPeepFormWhenLoggedIn() throws Exception {
        mockMvc.perform(get("/peeps/new").sessionAttr(SESSION_USERNAME, "alex1"))
                .andExpect(status().isOk())
                .andExpect(view().name("peeps/new"))
                .andExpect(content().string(containsString("name=\"message\"")));
    }

    @Test
    void postsAPeepAndRedirectsToTheStreamWhenLoggedIn() throws Exception {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(userService.findByUsername("alex1")).thenReturn(Optional.of(alex));

        mockMvc.perform(post("/peeps").sessionAttr(SESSION_USERNAME, "alex1").param("message", "Hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peeps"));

        verify(peepService).post(alex, "Hello");
    }

    @Test
    void postPeepRedirectsToLoginWhenNotLoggedIn() throws Exception {

        mockMvc.perform(post("/peeps").param("message", "Hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions/new"));

        verifyNoInteractions(userService, peepService);
    }

    @Test
    void postPeepInvalidatesSessionAndRedirectsWhenUserDoesNotExist() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SESSION_USERNAME, "ghost");

        when(userService.findByUsername("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(post("/peeps").session(session).param("message", "Hello"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sessions/new"));

        verify(userService).findByUsername("ghost");
        assertTrue(session.isInvalid());
        verifyNoInteractions(peepService);
    }

    @Test
    void postPeepRedisplaysFormWithErrorWhenMessageIsInvalid() throws Exception {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(userService.findByUsername("alex1")).thenReturn(Optional.of(alex));
        doThrow(new InvalidPeepException("Test error text"))
                .when(peepService).post(alex, "my draft peep");

        mockMvc.perform(post("/peeps")
                        .sessionAttr(SESSION_USERNAME, "alex1")
                        .param("message", "my draft peep"))
                .andExpect(status().isOk())
                .andExpect(view().name("peeps/new"))
                .andExpect(model().attribute("error", "Test error text"))
                .andExpect(model().attribute("message", "my draft peep"));
    }
    @Test
    void postPeepRendersErrorAndDraftInHtmlWhenMessageIsInvalid() throws Exception {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(userService.findByUsername("alex1")).thenReturn(Optional.of(alex));
        doThrow(new InvalidPeepException("Test error text"))
                .when(peepService).post(alex, "my draft peep");

        mockMvc.perform(post("/peeps")
                        .sessionAttr(SESSION_USERNAME, "alex1")
                        .param("message", "my draft peep"))
                .andExpect(status().isOk())
                .andExpect(view().name("peeps/new"))
                .andExpect(content().string(containsString("Test error text")))
                .andExpect(content().string(containsString("my draft peep")));
    }

    @Test
    void postPeepEscapesHtmlInDraftWhenRedisplayingForm() throws Exception {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(userService.findByUsername("alex1")).thenReturn(Optional.of(alex));
        doThrow(new InvalidPeepException("Test error text"))
                .when(peepService).post(alex, "<b>hi</b>");

        mockMvc.perform(post("/peeps")
                        .sessionAttr(SESSION_USERNAME, "alex1")
                        .param("message", "<b>hi</b>"))
                .andExpect(status().isOk())
                .andExpect(view().name("peeps/new"))
                .andExpect(content().string(containsString("&lt;b&gt;hi&lt;/b&gt;")))
                .andExpect(content().string(not(containsString("<b>hi</b>"))));
    }

    @Test
    void rootRedirectsToPeeps() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peeps"));
    }
}