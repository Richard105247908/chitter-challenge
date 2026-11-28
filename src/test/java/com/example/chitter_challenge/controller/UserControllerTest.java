package com.example.chitter_challenge.controller;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.DuplicateUserException;
import com.example.chitter_challenge.service.InvalidUserException;
import com.example.chitter_challenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MvcResult;


import static com.example.chitter_challenge.controller.UserSession.SESSION_USERNAME;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
                .andExpect(content().string(containsString("type=\"password\"")))
                .andExpect(content().string(containsString("minlength=\"8\"")));
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
        assertEquals("alex_saved", session.getAttribute(SESSION_USERNAME));
    }

    @Test
    void postUsersRedisplaysFormWithoutPasswordWhenUserIsDuplicate() throws Exception {
        doThrow(new DuplicateUserException("Test duplicate error"))
                .when(userService).signUp("alex@example.com", "password123", "Alex", "alex1");

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/users")
                        .session(session)
                        .param("email", "alex@example.com")
                        .param("password", "password123")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/new"))
                .andExpect(model().attribute("error", "Test duplicate error"))
                .andExpect(model().attribute("email", "alex@example.com"))
                .andExpect(model().attribute("name", "Alex"))
                .andExpect(model().attribute("username", "alex1"))
                .andExpect(model().attributeDoesNotExist("password"));

        assertNull(session.getAttribute(SESSION_USERNAME));
    }

    @Test
    void postUsersRendersErrorAndTypedFieldsWithoutPasswordInHtml() throws Exception {
        doThrow(new DuplicateUserException("Test duplicate error"))
                .when(userService).signUp("alex@example.com", "password123", "Alex", "alex1");

        mockMvc.perform(post("/users")
                        .param("email", "alex@example.com")
                        .param("password", "password123")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/new"))
                .andExpect(content().string(containsString("Test duplicate error")))
                .andExpect(content().string(containsString("value=\"alex@example.com\"")))
                .andExpect(content().string(containsString("value=\"Alex\"")))
                .andExpect(content().string(containsString("value=\"alex1\"")))
                .andExpect(content().string(not(containsString("password123"))));
    }
    @Test
    void signUpChangesTheSessionId() throws Exception {
        User saved = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(userService.signUp("alex@example.com", "password123", "Alex", "alex1"))
                .thenReturn(saved);

        MockHttpSession session = new MockHttpSession();
        String before = session.getId();

        MvcResult result = mockMvc.perform(post("/users")
                        .session(session)
                        .param("email", "alex@example.com")
                        .param("password", "password123")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andReturn();
        assertNotEquals(before, result.getRequest().getSession().getId());
    }

    @Test
    void postUsersRedisplaysFormWithErrorWhenSignUpIsInvalid() throws Exception {
        when(userService.signUp("alex@example.com", "short", "Alex", "alex1"))
                .thenThrow(new InvalidUserException("Test invalid error"));

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/users")
                        .session(session)
                        .param("email", "alex@example.com")
                        .param("password", "short")
                        .param("name", "Alex")
                        .param("username", "alex1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/new"))
                .andExpect(model().attribute("error", "Test invalid error"))
                .andExpect(model().attribute("email", "alex@example.com"))
                .andExpect(model().attribute("name", "Alex"))
                .andExpect(model().attribute("username", "alex1"))
                .andExpect(model().attributeDoesNotExist("password"));

        assertNull(session.getAttribute(UserSession.SESSION_USERNAME));
    }

}