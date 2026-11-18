package com.example.chitter_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
class SchemaTest {

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void usersTableStartsEmpty() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        assertEquals(0, count);
    }

    @Test
    void rejectsDuplicateEmail() {
        insertUser("alex@example.com", "alex1");

        assertThrows(DataIntegrityViolationException.class, () -> {
            insertUser("alex@example.com", "bob1");
        });
    }

    @Test
    void rejectsDuplicateUsername() {
        insertUser("alex@example.com", "alex1");

        assertThrows(DataIntegrityViolationException.class, () ->
                insertUser("bob@example.com", "alex1"));
    }

    @Test
    void rejectsPeepFromUnknownUser() {
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update(
                        "INSERT INTO peeps (user_id, message, created_at) VALUES (?, ?, ?)",
                        999L, "Hello world", OffsetDateTime.now()
                ));
    }

    @Test
    void acceptsPeepFromKnownUser() {

        insertUser("alex@example.com", "alex1");


        Long userId = jdbc.queryForObject("SELECT id FROM users WHERE username = ?",
                Long.class,
                "alex1"
        );

        jdbc.update("INSERT INTO peeps (user_id, message, created_at) VALUES (?,?,?)",
                userId, "Hello world", OffsetDateTime.now());

        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM peeps", Integer.class);
        assertEquals(1, count);
    }


    private void insertUser(String email, String username) {
        jdbc.update("INSERT INTO users (email, password_hash, name, username) VALUES (?, ?, ?, ?)",
                email, "hashed_pw", "Test User", username
        );
    }
}