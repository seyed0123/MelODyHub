package com.example.melodyhub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Podcaster extends Account implements Serializable {
    private boolean verify;
    private String bio;

    private double rate;

    public Podcaster(String uuid,String username, String password, String email, String phoneNumber, String image, boolean verify, String bio,double rate) {
        super(uuid,username, password, email, phoneNumber, image);
        this.bio=bio;
        this.verify=verify;
        this.rate=rate;
    }
    @JsonCreator
    public static Podcaster createFromJson(@JsonProperty("id") String id,
                                           @JsonProperty("username") String username,
                                           @JsonProperty("password") String password,
                                           @JsonProperty("email") String email,
                                           @JsonProperty("phoneNumber") String phoneNumber,
                                           @JsonProperty("image") String image,
                                           @JsonProperty("verify") boolean verify,
                                           @JsonProperty("bio") String bio,
                                           @JsonProperty("rate") double rate) {
        return new Podcaster(id, username, password, email, phoneNumber, image, verify, bio, rate);
    }
    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify=verify;
    }

    public String getBio() {
        return bio;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setBio(String bio) {
        this.bio=bio;
    }
    @Override
    public String toString() {
        return "Podcast{" +
                "verify=" + isVerify() +
                ", bio='" + getBio() + '\'' +
                "} " + super.toString();
    }
}
