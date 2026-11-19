package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.Peep;
import com.example.chitter_challenge.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public class PeepRepository {
    private final JdbcTemplate jdbc;

    public PeepRepository (JdbcTemplate jdbc){
        this.jdbc=jdbc;
    }

    public Peep save(User author, String message, OffsetDateTime createdAt){
        return null;
    }
}
