package com.example.melodyhub.Server.MelodyHub;

import java.util.UUID;

public class Podcast extends Song{
    private String description;

    public Podcast(String name, String genre, double duration, int year, double rate, String lyrics, String description) {
        super( name, genre, duration, year, rate, lyrics);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        MelodyHub.sendQuery("update song set description = '"+description+"' where id = '"+getId()+"';");
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "description='" + description + '\'' +
                "} " + super.toString();
    }
}
