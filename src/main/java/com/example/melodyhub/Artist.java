package com.example.melodyhub;

public class Artist extends Account{
    private String bio;
    private boolean verify;

    private int listeners;

    private double rate;

    private String mainGenre;

    public Artist(String uuid,String username, String password, String email, String phoneNumber, String image, boolean verify, String bio,int listeners,double rate,String mainGenre) {
        super(uuid,username, password, email, phoneNumber, image);
        this.bio=bio;
        this.verify=verify;
        this.listeners = listeners;
        this.rate=rate;
        this.mainGenre=mainGenre;
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
