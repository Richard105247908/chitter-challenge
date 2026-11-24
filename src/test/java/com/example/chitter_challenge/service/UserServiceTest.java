package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository users;

    PasswordEncoder encoder = new BCryptPasswordEncoder();
    UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(users, encoder);
    }

    @Test
    void signUpStoresAHashedPasswordNotThePlainOne() {
        service.signUp("alex@example.com", "password123", "Alex", "alex1");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(users).save(captor.capture());
        User saved = captor.getValue();

        assertNotEquals("password123", saved.getPasswordHash());
        assertTrue(encoder.matches("password123", saved.getPasswordHash()));
        assertEquals("alex@example.com", saved.getEmail());
        assertEquals("Alex", saved.getName());
        assertEquals("alex1", saved.getUsername());
    }

    @Test
    void signUpRejectsATakenUsernameWithoutSaving() {
        User existing = new User(1L, "other@example.com", "hash", "Other", "alex1");
        when(users.findByUsername("alex1")).thenReturn(Optional.of(existing));

        DuplicateUserException e = assertThrows(DuplicateUserException.class,
                () -> service.signUp("alex@example.com", "password123", "Alex", "alex1"));

        assertEquals("That username is already taken", e.getMessage());
        verify(users, never()).save(any());
    }

    @Test
    void signUpRejectedATakenEmailWithoutSaving() {
        User existing = new User(1L, "other@example.com", "hash", "Other", "alex1");
        when(users.findByEmail("other@example.com")).thenReturn(Optional.of(existing));

        DuplicateUserException e = assertThrows(DuplicateUserException.class,
                () -> service.signUp("other@example.com", "password123", "Alex", "alex1"));

        assertEquals("That email is already taken", e.getMessage());
        verify(users, never()).save(any());

    }
}
