package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void findsTheRightUserWhenSeveralExist() {
        repository.save(new User("alex@example.com", "hashed_pw", "Alex", "alex1"));
        repository.save(new User("bob@example.com", "hashed_pw2", "Bob", "bob1"));
        Optional<User> found = repository.findByUsername("bob1");

        assertTrue(found.isPresent());
        assertEquals("bob@example.com", found.get().getEmail());
    }

    @Test
    void returnsEmptyWhenUsernameDoesNotExist() {
        repository.save(new User("alex@example.com", "hashed_pw", "Alex", "alex1"));

        Optional<User> found = repository.findByUsername("nobody");
        assertTrue(found.isEmpty());
    }

    @Test
    void savedUserHasTheGeneratedId() {
        User saved = repository.save(new User(1L,"alex@example.com", "hashed_pw", "Alex", "alex1"));
       User saved2= repository.save(new User("bob@example.com", "hashed_pw2", "Bob", "bob1"));

        Optional<User> found = repository.findByUsername("alex1");
        Optional<User> found2 = repository.findByUsername("bob1");

        assertTrue(found.isPresent());

        assertNotNull(saved.getId());
         assertNotNull(saved.getEmail());
         assertNotNull(saved.getUsername());
        assertEquals(found2.get().getId(), saved2.getId());


    }
    @Test

}
