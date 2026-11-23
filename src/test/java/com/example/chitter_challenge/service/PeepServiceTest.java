package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.PeepRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PeepServiceTest {
    @Mock
    PeepRepository peeps;

    @Test
    void postStampsThePeepWithTheCurrentTime(){
        Clock clock=Clock.fixed(Instant.parse("2026-11-19T10:00:00Z"), ZoneOffset.UTC);
        PeepService service= new PeepService(peeps,clock);

        User alex = new User(1L,"alex@example.com", "hashed_pw", "Alex", "alex1");

        service.post(alex,"Hello world");
        verify(peeps).save(alex, "Hello world", OffsetDateTime.parse("2026-11-19T10:00:00Z"));
    }

}
