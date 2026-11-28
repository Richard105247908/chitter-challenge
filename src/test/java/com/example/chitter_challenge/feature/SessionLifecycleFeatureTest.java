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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SessionLifecycleFeatureTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void aMakerCanLogOutAndLogBackInToSeeTheirEarlierPeep() throws Exception {
        MockHttpSession browser = new MockHttpSession();

        mockMvc.perform(post("/users").session(browser)
                        .param("email", "alex@example.com")
                        .param("password", "password123")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andExpect(redirectedUrl("/peeps"));

        mockMvc.perform(post("/peeps").session(browser)
                        .param("message", "Persistent peep"))
                .andExpect(redirectedUrl("/peeps"));

        mockMvc.perform(post("/sessions/delete").session(browser))
                .andExpect(redirectedUrl("/peeps"));

        assertTrue(browser.isInvalid());

        MockHttpSession loggedOutBrowser = new MockHttpSession();

        mockMvc.perform(get("/peeps").session(loggedOutBrowser))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("href=\"/sessions/new\"")))
                .andExpect(content().string(not(containsString("action=\"/sessions/delete\""))))
                .andExpect(content().string(containsString("Persistent peep")));

        mockMvc.perform(post("/sessions").session(loggedOutBrowser)
                        .param("username", "alex1")
                        .param("password", "password123"))
                .andExpect(redirectedUrl("/peeps"));

        mockMvc.perform(get("/peeps").session(loggedOutBrowser))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("action=\"/sessions/delete\"")))
                .andExpect(content().string(not(containsString("href=\"/sessions/new\""))))
                .andExpect(content().string(containsString("Persistent peep")));
    }
}