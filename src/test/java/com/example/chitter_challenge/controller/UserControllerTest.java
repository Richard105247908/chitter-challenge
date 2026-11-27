package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;


import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Test
    void showsTheSignUpForm() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/new"))
                .andExpect(content().string(containsString("name=\"email\"")))
                .andExpect(content().string(containsString("name=\"name\"")))
                .andExpect(content().string(containsString("name=\"username\"")))
                .andExpect(content().string(containsString("type=\"password\"")));
    }

    @Test
    void postUsersSignsUpLogsInReturnedUserAndRedirects() throws Exception {
        User saved = new User(1L, "alex@example.com", "hash", "Alex", "alex_saved");
        when(userService.signUp("alex@example.com", "password123", "Alex", "alex1"))
                .thenReturn(saved);

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/users")
                        .session(session)
                        .param("email", "alex@example.com")
                        .param("password", "password123")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/peeps"));

        verify(userService).signUp("alex@example.com", "password123", "Alex", "alex1");
        assertEquals("alex_saved", session.getAttribute(PeepController.SESSION_USERNAME));
    }
}