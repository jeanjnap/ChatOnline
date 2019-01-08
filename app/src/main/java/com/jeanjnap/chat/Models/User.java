package com.jeanjnap.chat.Models;

public class User {

    public String name;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name) {
        this.name = name;
    }
}