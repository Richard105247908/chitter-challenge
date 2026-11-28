package com.example.chitter_challenge.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DuplicateSignUpFeatureTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void signingUpWithATakenUsernameShowsAnErrorAndDoesNotLogIn() throws Exception {
        MockHttpSession firstBrowser = new MockHttpSession();

        mockMvc.perform(post("/users").session(firstBrowser)
                        .param("email", "alex@example.com")
                        .param("password", "password123")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andExpect(redirectedUrl("/peeps"));

        MockHttpSession secondBrowser = new MockHttpSession();

        mockMvc.perform(post("/users").session(secondBrowser)
                        .param("email", "other@example.com")
                        .param("password", "password123")
                        .param("name", "Other")
                        .param("username", "alex1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("That username is already taken")));

        mockMvc.perform(get("/peeps").session(secondBrowser))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("href=\"/sessions/new\"")))
                .andExpect(content().string(not(containsString("action=\"/sessions/delete\""))));
    }
}