package com.example.chitter_challenge.repository;

import com.example.chitter_challenge.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(PeepRepository.class)

public class PeepRepositoryTest {

    @Autowired
    UserRepository users;
    @Autowired
    PeepRepository peeps;

    @Test
    void savedPeepComesBackWithItsAuthorsNameAndUsername() {
        User alex=
    }

}
