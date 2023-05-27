package com.example.melodyhub.Server.MelodyHub;

import java.util.ArrayList;
import java.util.UUID;

public class PlayList {
    private UUID id;
    private boolean personal;
    private double rate;

    public PlayList( boolean personal, double rate) {
        this.id = UUID.randomUUID();
        this.personal = personal;
        this.rate = rate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public ArrayList<UUID> getSongs() {
        return null;
    }

    public void addSong(UUID song) {
    }

    public void removeSong(UUID song) {
    }

    public void changeSongOrder(UUID song,int order) {

    }

    public ArrayList<UUID> getOwners() {
        return null;
    }

    public void addOwner(UUID user) {

    }

    public void removeOwner(UUID user) {

    }

    public void shuffle()
    {

    }
}
