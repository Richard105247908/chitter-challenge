package com.example.chitter_challenge.service;

import com.example.chitter_challenge.repository.PeepRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PeepServiceTest {
    @Mock
    PeepRepository peeps;

}
