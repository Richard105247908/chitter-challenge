package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Test
    void raceSignUpDataIntegrityDuplicateUserException() {
        when(users.save(any())).thenThrow(new DataIntegrityViolationException("duplicate key"));

        DuplicateUserException e = assertThrows(DuplicateUserException.class,
                () -> service.signUp("alex@example.com", "password123", "Alex", "alex1"));

        assertEquals("That username or email is already in use", e.getMessage());
    }
    @Test
    void findsAUserByUsername() {
        User alex = new User(1L, "alex@example.com", "hash", "Alex", "alex1");
        when(users.findByUsername("alex1")).thenReturn(Optional.of(alex));

        assertEquals(Optional.of(alex), service.findByUsername("alex1"));
    }

    @Test
    void signUpReturnsTheSavedUser() {
        User saved = new User(7L, "alex@example.com", "hash", "Alex", "alex1");
        when(users.save(any())).thenReturn(saved);

        User result = service.signUp("alex@example.com", "password123", "Alex", "alex1");

        assertSame(saved, result);
    }

    @Test
    void authenticateReturnsTheUserWhenThePasswordMatches() {
        User alex = new User(1L, "alex@example.com", encoder.encode("password123"), "Alex", "alex1");
        when(users.findByUsername("alex1")).thenReturn(Optional.of(alex));

        Optional<User> result = service.authenticate("alex1", "password123");

        assertTrue(result.isPresent());
        assertSame(alex, result.get());
    }

    @Test
    void authenticateReturnsEmptyWhenPasswordIsWrong() {
        User alex = new User(1L, "alex@example.com", encoder.encode("password123"), "Alex", "alex1");
        when(users.findByUsername("alex1")).thenReturn(Optional.of(alex));

        Optional<User> result = service.authenticate("alex1", "wrong-password");

        assertTrue(result.isEmpty());
    }

    @Test
    void authenticateReturnsEmptyWhenUsernameDoesNotExist() {
        when(users.findByUsername("ghost")).thenReturn(Optional.empty());

        Optional<User> result = service.authenticate("ghost", "password123");

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void signUpRejectsBlankOrNullName(String blankName) {
        InvalidUserException ex = assertThrows(
                InvalidUserException.class,
                () -> service.signUp("alex@example.com", "secret123", blankName, "alex1"));

        String msg = ex.getMessage().toLowerCase();
        assertTrue(msg.contains("name is required"));
        assertFalse(msg.contains("username"));
        verifyNoInteractions(users);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void signUpRejectsBlankOrNullEmail(String blankEmail) {
        InvalidUserException ex = assertThrows(
                InvalidUserException.class,
                () -> service.signUp(blankEmail, "secret123", "Alex", "alex1"));

        String msg = ex.getMessage().toLowerCase();
        assertTrue(msg.contains("email"));
        assertFalse(msg.contains("username"));
        verifyNoInteractions(users);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void signUpRejectsBlankOrNullPassword(String blankPassword) {
        InvalidUserException ex = assertThrows(
                InvalidUserException.class,
                () -> service.signUp("alex@example.com", blankPassword, "Alex", "alex1"));

        String msg = ex.getMessage().toLowerCase();
        assertTrue(msg.contains("password"));
        verifyNoInteractions(users);
    }


}
