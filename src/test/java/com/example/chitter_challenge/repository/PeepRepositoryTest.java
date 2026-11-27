package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.Peep;
import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.service.InvalidPeepException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.security.MessageDigest;
import java.sql.Time;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({UserRepository.class, PeepRepository.class})
public class PeepRepositoryTest {

    @Autowired
    UserRepository users;
    @Autowired
    PeepRepository peeps;

    @Test
    void savedPeepComesBackWithItsAuthorsNameAndUsername() {
        User alex = users.save(new User("alex@example.com", "hashed_pw", "Alex", "alex1"));

        peeps.save(alex, "Hello world", OffsetDateTime.now());
        List<Peep> all = peeps.findAllNewestFirst();

        assertEquals(1, all.size());
        assertEquals("Hello world", all.get(0).getMessage());
        assertEquals("Alex", all.get(0).getAuthorName());
        assertEquals("alex1", all.get(0).getAuthorUsername());

    }

    @Test
    void peepsFindAllNewestFirst() {
        User author = users.save(new User("alex@example.com", "hashed_pw", "Alex", "alex1"));

        peeps.save(author, "Middle peep", OffsetDateTime.parse("2026-11-19T11:00:00Z"));
        peeps.save(author, "Newest peep", OffsetDateTime.parse("2026-11-19T12:00:00Z"));
        peeps.save(author, "Oldest peep", OffsetDateTime.parse("2026-11-19T10:00:00Z"));

        List<Peep> all = peeps.findAllNewestFirst();

        assertEquals(3, all.size());
        assertEquals("Newest peep", all.get(0).getMessage());
        assertEquals("Middle peep", all.get(1).getMessage());
        assertEquals("Oldest peep", all.get(2).getMessage());

        assertEquals(
                List.of("Newest peep", "Middle peep", "Oldest peep"),
                all.stream().map(Peep::getMessage).toList());
    }

    @Test
    void savedPeepIsReturnedWithItsGeneratedId() {
        OffsetDateTime createdAt = OffsetDateTime.parse("2026-11-19T10:00:00Z");
        OffsetDateTime createdAt2 = OffsetDateTime.parse("2026-11-19T11:00:00Z");

        User alex = users.save(new User("alex@example.com", "hashed_pw", "Alex", "alex1"));
        Peep saved = peeps.save(alex, "Hello World", createdAt);
        Peep saved2 = peeps.save(alex, "Hello Universe", createdAt2);

        List<Peep> loaded = peeps.findAllNewestFirst();

        assertEquals(loaded.get(1).getId(), saved.getId());
        assertEquals(loaded.get(0).getId(), saved2.getId());

        assertEquals("Hello World", saved.getMessage());
        assertEquals("Alex", saved.getAuthorName());
        assertEquals("alex1", saved.getAuthorUsername());
        assertTrue(createdAt.isEqual(saved.getCreatedAt()));

        assertEquals("Hello Universe", saved2.getMessage());
        assertEquals("Alex", saved2.getAuthorName());
        assertEquals("alex1", saved2.getAuthorUsername());
    }

}
