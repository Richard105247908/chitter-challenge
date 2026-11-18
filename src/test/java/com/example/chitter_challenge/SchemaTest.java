package com.example.chitter_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

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
        jdbc.update(
                "INSERT INTO users (email, password_hash, name, username) VALUES (?, ?, ?, ?)",
                "alex@example.com", "hashed_pw_1", "Alex", "alex1"
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            jdbc.update(
                    "INSERT INTO users (email, password_hash, name, username) VALUES (?, ?, ?, ?)",
                    "alex@example.com", "hashed_pw_2", "Bob", "bob1"
            );
        });
    }
}