package com.example.melodyhub.Server.MelodyHub;

import java.util.ArrayList;
import java.util.UUID;

public class Artist extends Account{

    public Artist(String username, String password, String email, String phoneNumber, String image, boolean verify, String bio) {
        super(username, password, email, phoneNumber, image);
    }

    public boolean isVerify() {
        return false;
    }

    public void setVerify(boolean verify) {

    }

    public String getBio() {
        return null;
    }

    public void setBio(String bio) {

    }

    public ArrayList<UUID> getSongs() {
        return null;
    }

    public void addSong(UUID song) {
    }

    public void removeSong(UUID song) {
    }

    public void updateSong(UUID song) {
    }

    @Override
    public String toString() {
        return "Artist{" +
                "verify=" + isVerify() +
                ", bio='" + getBio() + '\'' +
                "} " + super.toString();
    }
}
