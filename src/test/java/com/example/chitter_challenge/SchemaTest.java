package com.example.chitter_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
class SchemaTest {

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void usersTableStartsEmpty() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        assertEquals(0, count);
    }
}