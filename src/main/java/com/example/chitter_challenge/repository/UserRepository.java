package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User save(User user) {

    }

    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }
}




