package com.example.chitter_challenge;

public class User {
    private String email;
    private String password_hash;
    private String name;
    private String userName;

    public User(){
    }
    public User(String name){
        this.name=name;
    }

    public User(String email, String password_hash, String name, String userName) {
        this.email = email;
        this.password_hash = password_hash;
        this.name = name;
        this.userName = userName;
    }


}
