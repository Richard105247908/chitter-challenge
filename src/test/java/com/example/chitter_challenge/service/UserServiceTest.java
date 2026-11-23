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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository users;

    PasswordEncoder encoder= new BCryptPasswordEncoder();
    UserService service;

    @BeforeEach
    void setUp() {
        service= new UserService(users,encoder);
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
}
