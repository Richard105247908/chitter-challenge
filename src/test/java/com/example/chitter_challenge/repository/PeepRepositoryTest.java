package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.Peep;
import com.example.chitter_challenge.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
