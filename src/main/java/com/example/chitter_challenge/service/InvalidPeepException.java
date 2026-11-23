package com.example.chitter_challenge.service;

public class InvalidPeepException extends RuntimeException{
    public InvalidPeepException(String message) {
        super(message);
    }
}
