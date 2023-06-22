package com.example.melodyhub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class PlayList implements Serializable {
    private String id;
    private double duration;
    private String name;
    private boolean personal;
    private double rate;

    private UUID artist;

    private UUID firstOwner;
    private ArrayList<UUID> songs;

    public PlayList(String id,String name, boolean personal, double rate,double duration,UUID artist,String firstOwner) {
        this.id=id;
        this.name=name;
        this.personal = personal;
        this.rate = rate;
        this.duration=duration;
        if(artist!=null)
            this.artist=artist;
        this.firstOwner=UUID.fromString(firstOwner);
        songs=new ArrayList<>();
    }
    @JsonCreator
    public static PlayList createFromJson(@JsonProperty("id") String id,
                                          @JsonProperty("name") String name,
                                          @JsonProperty("duration") double duration,
                                          @JsonProperty("personal") boolean personal,
                                          @JsonProperty("rate") double rate,
                                          @JsonProperty("artist") UUID artist,
                                          @JsonProperty("firstOwner") String firstOwner,
                                          @JsonProperty("songs") ArrayList<UUID> songs) {
        PlayList playList = new PlayList(id,name, personal, rate, duration, artist, firstOwner);
        playList.setSongs(songs);
        return playList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return UUID.fromString(id);
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
