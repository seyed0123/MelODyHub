package com.example.melodyhub.Server.MelodyHub;

import java.util.ArrayList;
import java.util.UUID;

public class Podcaster extends Account{

    public Podcaster(String username, String password, String email, String phoneNumber, String image, boolean verify, String bio) {
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

    public ArrayList<UUID> getPodcasts() {
        return null;
    }

    public void addPodcast(UUID Podcast) {
    }

    public void removePodcast(UUID Podcast) {
    }

    public void updatePodcast(UUID Podcast) {
    }
}
