package com.example.chitter_challenge.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostingAPeepFeatureTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void aNewMakerCanSignUpPostAPeepAndSeeItInTheStream() throws Exception {
        MockHttpSession browser = new MockHttpSession();

        mockMvc.perform(post("/users").session(browser)
                        .param("email", "alex@example.com")
                        .param("password", "password123")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andExpect(redirectedUrl("/peeps"));

        mockMvc.perform(post("/peeps").session(browser).param("message", "Hello from the feature test"))
                .andExpect(redirectedUrl("/peeps"));

        mockMvc.perform(get("/peeps").session(browser))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello from the feature test")))
                .andExpect(content().string(containsString("Alex (@alex1)")))
                .andExpect(content().string(containsString("action=\"/sessions/delete\"")));
    }
}