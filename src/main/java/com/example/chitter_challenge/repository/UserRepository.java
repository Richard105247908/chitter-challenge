package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (email, password_hash, name, username) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getName());
            ps.setString(4, user.getUsername());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new User(id, user.getEmail(), user.getPasswordHash(), user.getName(), user.getUsername());
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

    public Optional<User> findByEmail(String email) {
        List<User> users = jdbc.query(
                "SELECT id, email, password_hash, name, username FROM users WHERE email = ?",
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("name"),
                        rs.getString("username")
                ),
                email
        );
        return users.stream().findFirst();
    }
}




