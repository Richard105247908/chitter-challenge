package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.Peep;
import com.example.chitter_challenge.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class PeepRepository {
    private final JdbcTemplate jdbc;

    public PeepRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Peep save(User author, String message, OffsetDateTime createdAt) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO peeps (user_id, message, created_at ) VALUES (?, ?, ?)",
                    new String[]{"id"});
            ps.setLong(1, author.getId());
            ps.setString(2, message);
            ps.setObject(3, createdAt);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Peep(id, message, createdAt, author.getName(), author.getUsername());
    }

    public List<Peep> findAllNewestFirst() {
        String sql = """
                SELECT peeps.id, peeps.message, peeps.created_at, users.name, users.username
                FROM peeps
                JOIN users ON peeps.user_id = users.id
                ORDER BY peeps.created_at DESC
                """;

        return jdbc.query(sql, (rs, rowNum) -> new Peep(
                rs.getLong("id"),
                rs.getString("message"),
                rs.getObject("created_at", OffsetDateTime.class),
                rs.getString("name"),
                rs.getString("username")
        ));
    }
}
