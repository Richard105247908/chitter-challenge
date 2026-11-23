package com.example.chitter_challenge.service;

import com.example.chitter_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository users;

    PasswordEncoder encoder= new BCryptPasswordEncoder();
    UserService service;

    @BeforeEach
    void setUp() {
        service= new UserService(users,encoder);
    }

    @Test
    void signUpStoresAHashedPasswordNotThePlainOne(){

    }


}
