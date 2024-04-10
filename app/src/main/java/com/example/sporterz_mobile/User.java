package com.example.sporterz_mobile;

import android.net.Uri;

public class User {
    private String firstname;
    private String lastname;
    private String username;
    private String bio;

    public User(String firstname, String lastname, String username, String bio) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.bio = bio;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() { return this.firstname; }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return this.bio;
    }
}
