package com.jeanjnap.chat.Models;

public class Contact extends User {
    private String email;

    public Contact() {
    }

    public Contact(String email) {
    }

    public Contact(String nome, String email) {
        super(nome);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
