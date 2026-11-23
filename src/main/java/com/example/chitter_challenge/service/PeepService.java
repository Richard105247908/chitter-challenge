package com.example.chitter_challenge.service;

import com.example.chitter_challenge.model.User;
import com.example.chitter_challenge.repository.PeepRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Service
public class PeepService {
    private final PeepRepository peepRepository;
    private final Clock clock;
    private static final int MAX_LENGTH=200;

    public PeepService(PeepRepository peepRepository, Clock clock) {
        this.peepRepository = peepRepository;
        this.clock = clock;

    }

    public void post(User author, String message) {

        if (message == null || message.isBlank()|| message.length()>MAX_LENGTH){
            throw new InvalidPeepException("Peeps must be 1 to " +MAX_LENGTH + " characters");
        }
        peepRepository.save(author, message, OffsetDateTime.now(clock));
    }
}
