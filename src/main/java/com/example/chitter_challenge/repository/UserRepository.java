package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void save(User user) {

        jdbc.update("INSERT INTO users (email, password_hash,name,username) VALUES (?,?,?,?)",
                user.getEmail(),user.getPasswordHash(), user.getName(), user.getUsername()
        );
    }

    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }
}




