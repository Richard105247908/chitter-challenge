package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.User;
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

    public Optional<User> findByName(String name) {
        List<User> users = jdbc.query(
                "SELECT name FROM users WHERE name=?",
                (rs, rowNum) -> new User(rs.getString("name")),
                name);
        return users.stream().findFirst();
    }

}




