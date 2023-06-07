package com.example.melodyhub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Artist extends Account implements Serializable {
    private String bio;
    private boolean verify;

    private int listeners;

    private double rate;

    private String mainGenre;

    public Artist(String uuid, String username, String password, String email, String phoneNumber, String image, String bio, boolean verify, int listeners, double rate, String mainGenre) {
        super(uuid, username, password, email, phoneNumber, image);
        this.bio = bio;
        this.verify = verify;
        this.listeners = listeners;
        this.rate = rate;
        this.mainGenre = mainGenre;
    }
    @JsonCreator
    public static Artist createFromJson(@JsonProperty("id") String id,
                                        @JsonProperty("username") String username,
                                        @JsonProperty("password") String password,
                                        @JsonProperty("email") String email,
                                        @JsonProperty("phoneNumber") String phoneNumber,
                                        @JsonProperty("image") String image,
                                        @JsonProperty("bio") String bio,
                                        @JsonProperty("verify") boolean verify,
                                        @JsonProperty("listeners") int listeners,
                                        @JsonProperty("rate") double rate,
                                        @JsonProperty("mainGenre") String mainGenre) {
        return new Artist(id, username, password, email, phoneNumber, image, bio, verify, listeners, rate, mainGenre);
    }
    public boolean isVerify() {
        return verify;
    }

    public int getListeners() {
        return listeners;
    }

    public void setListeners(int listeners) {
        this.listeners = listeners;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setVerify(boolean verify) {
        this.verify=verify;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio=bio;
    }

    public String getMainGenre() {
        return mainGenre;
    }

    public void setMainGenre(String mainGenre) {
        this.mainGenre = mainGenre;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "verify=" + isVerify() +
                ", bio='" + getBio() + '\'' +
                "} " + super.toString();
    }
}
