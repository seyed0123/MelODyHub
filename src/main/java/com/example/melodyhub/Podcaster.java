package com.example.melodyhub;

public class Podcaster extends Account{
    private boolean verify;
    private String bio;

    private double rate;

    public Podcaster(String uuid,String username, String password, String email, String phoneNumber, String image, boolean verify, String bio,double rate) {
        super(uuid,username, password, email, phoneNumber, image);
        this.bio=bio;
        this.verify=verify;
        this.rate=rate;
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
