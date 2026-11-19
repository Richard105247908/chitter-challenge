package com.example.chitter_challenge.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PeepRepository {
    private final JdbcTemplate jdbc;

    public PeepRepository (JdbcTemplate jdbc){
        this.jdbc=jdbc;
    }
}
