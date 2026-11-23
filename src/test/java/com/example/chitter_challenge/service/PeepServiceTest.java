package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.PeepRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PeepServiceTest {
    @Mock
    PeepRepository peeps;

    private Clock clock;
    private PeepService service;
    private User alex;

    @BeforeEach
    void setup() {
        clock = Clock.fixed(Instant.parse("2026-11-19T10:00:00Z"), ZoneOffset.UTC);
        service = new PeepService(peeps, clock);
        alex = new User(1L, "alex@example.com", "hashed_pw", "Alex", "alex1");

    }

    @Test
    void postStampsThePeepWithTheCurrentTime() {


        service.post(alex, "Hello world");
        verify(peeps).save(alex, "Hello world", OffsetDateTime.parse("2026-11-19T10:00:00Z"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void rejectsBlankOrNullMessages(String invalidMessage) {

        assertThrows(InvalidPeepException.class, () -> service.post(alex, invalidMessage));
        verifyNoInteractions(peeps);
    }

}
