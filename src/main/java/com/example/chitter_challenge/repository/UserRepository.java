package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User save(User user) {
        jdbc.update("INSERT INTO users (email, password_hash, name, username) VALUES (?, ?, ?, ?)",
                user.getEmail(), user.getPasswordHash(), user.getName(), user.getUsername()
        );
    }

    public Optional<User> findByUsername(String username) {
        List<User> users = jdbc.query("SELECT id, email, password_hash, name, username FROM users WHERE username = ?",
                (rs, rowNum) -> new User(rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("name"),
                        rs.getString("username")
                ),
                username
        );
        return users.stream().findFirst();
    }
}




