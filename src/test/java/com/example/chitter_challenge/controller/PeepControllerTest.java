package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.Peep;
import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.PeepService;
import com.example.chitter_challenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
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
        mockMvc.perform(get("/peeps/new").sessionAttr(PeepController.SESSION_USERNAME, "alex1"))
                .andExpect(status().isOk())
                .andExpect(view().name("peeps/new"))
                .andExpect(content().string(containsString("name=\"message\"")));
    }

    @Test
    void postsAPeepAndRedirectsToTheStreamWhenLoggedIn() throws Exception {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(userService.findByUsername("alex1")).thenReturn(Optional.of(alex));

        mockMvc.perform(post("/peeps").sessionAttr(PeepController.SESSION_USERNAME, "alex1").param("message", "Hello"))
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

}