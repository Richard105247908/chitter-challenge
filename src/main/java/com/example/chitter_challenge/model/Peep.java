package com.example.chitter_challenge.model;

import java.time.OffsetDateTime;

public class Peep {
    private final long id;
    private final String message;
    private final OffsetDateTime createdAt;
    private final String authorName;
    private final String authorUsername;


    public Peep(long id, String message, OffsetDateTime createdAt, String authorName, String authorUsername) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.authorName = authorName;
        this.authorUsername = authorUsername;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }
}
