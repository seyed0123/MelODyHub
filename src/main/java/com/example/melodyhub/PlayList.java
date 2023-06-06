package com.example.melodyhub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class PlayList implements Serializable {
    private UUID id;
    private double duration;
    private boolean personal;
    private double rate;

    private UUID artist;

    private UUID firstOwner;
    private ArrayList<UUID> songs;

    public PlayList(String id, boolean personal, double rate,double duration,String artist,String firstOwner) {
        this.id=UUID.fromString(id);
        this.personal = personal;
        this.rate = rate;
        this.duration=duration;
        this.artist=UUID.fromString(artist);
        this.firstOwner=UUID.fromString(firstOwner);
        songs=new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isPersonal() {
        return personal;
    }

    public void setPersonal(boolean personal) {
        this.personal = personal;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public UUID getArtist() {
        return artist;
    }

    public void setArtist(UUID artist) {
        this.artist = artist;
    }

    public UUID getFirstOwner() {
        return firstOwner;
    }

    public void setFirstOwner(UUID firstOwner) {
        this.firstOwner = firstOwner;
    }

    public ArrayList<UUID> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<UUID> songs) {
        this.songs = songs;
    }

    public static ArrayList<UUID> shuffle(ArrayList<UUID> songs)
    {
        ArrayList<UUID> ret = new ArrayList<>(songs);
        Collections.shuffle(ret);
        return ret;
    }
    public void shuffle()
    {
        Collections.shuffle(songs);
    }
}
