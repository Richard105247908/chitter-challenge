package com.example.chitter_challenge.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(PeepRepository.class)

public class PeepRepositoryTest {
    @Autowired
    PeepRepository peepRepository;

}
