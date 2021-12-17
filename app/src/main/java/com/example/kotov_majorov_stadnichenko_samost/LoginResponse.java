package com.example.kotov_majorov_stadnichenko_samost;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    private String user_id;
    private String email;
    private String username;

    public String getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
