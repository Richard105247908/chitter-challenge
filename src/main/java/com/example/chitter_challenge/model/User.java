package com.example.chitter_challenge.model;

public class User {
    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String name;
    private final String username;


    public User(String email, String passwordHash, String name, String username) {
        this(null, email, passwordHash, name, username);
    }

    public User(Long id, String email, String passwordHash, String name, String username) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}