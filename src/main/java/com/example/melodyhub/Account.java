package com.example.melodyhub;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Account {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String Image;

    public Account(String uuid ,String username, String password, String email, String phoneNumber, String image) {
        id=uuid;
        this.username = username;
        this.password = (password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        Image = image;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return UUID.fromString(id);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = (password);

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", Image='" + Image + '\'' +
                '}';
    }
}
