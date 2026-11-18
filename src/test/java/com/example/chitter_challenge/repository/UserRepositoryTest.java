package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(UserRepository.class)

public class UserRepositoryTest {
    @Autowired
    UserRepository repository;

    @Test
    void savesAUserAndFindsThemByUsername() {
        repository.save(new User("alex@example.com", "hashed_pw", "Alex", "alex1"));
        Optional<User> found = repository.findByUsername("alex1");

        assertTrue(found.isPresent());
        assertEquals("Alex", found.get().getName());
        assertEquals("alex@example.com", found.get().getEmail());
    }

}
