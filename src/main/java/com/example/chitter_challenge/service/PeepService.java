package com.example.chitter_challenge.service;

import com.example.chitter_challenge.repository.PeepRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class PeepService {
    private final PeepRepository peepRepository;
    private final Clock clock;

    public PeepService(PeepRepository peepRepository, Clock clock){

    }
}
