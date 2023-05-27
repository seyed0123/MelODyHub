package com.example.melodyhub.Server.MelodyHub;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Account {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String Image;

    public Account(String username, String password, String email, String phoneNumber, String image) {
        id = UUID.randomUUID();
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        Image = image;
    }
    private String hashPassword(String password)
    {
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }
    private boolean checkPassword(String plain,String hashed)
    {
        return BCrypt.checkpw(plain,hashed);
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public ArrayList<UUID> getFollowings()
    {
        return null;
    }

    public ArrayList<UUID> getFollowers()
    {
        return null;
    }

    public void follow(UUID user)
    {

    }

    public void unfollow(UUID user) throws Exception {
        try
        {

        }catch (Exception e) {
            throw new Exception();
        }
    }
    public void addPlaylist(UUID playlist)
    {

    }

    public void UpdatePlaylist(UUID playlist) throws Exception {
        try
        {

        }catch (Exception e) {
            throw new Exception();
        }
    }

    public void removePlaylist(UUID playlist) throws Exception {
        try
        {

        }catch (Exception e) {
            throw new Exception();
        }
    }

    public ArrayList<UUID> getPlaylists()
    {
        return null;
    }
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
