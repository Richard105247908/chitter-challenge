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

    public PeepService(PeepRepository peepRepository, Clock clock) {
        this.peepRepository = peepRepository;
        this.clock = clock;

    }

    public void post(User author, String message) {

        if (message == null || message.isBlank()){
            throw new InvalidPeepException("stop");
        }

        if (message.length() > 200) {
            throw new InvalidPeepException("Peeps must be 1 to 200 characters");
        }

        peepRepository.save(author, message, OffsetDateTime.now(clock));
    }
}
