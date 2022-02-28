package com.example.alarm.model;

public class User {
    int user_id;
    String username;
    String email_address;

    public User(int user_id, String username, String email_address) {
        this.user_id = user_id;
        this.username = username;
        this.email_address = email_address;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }
}
