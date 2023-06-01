package com.example.melodyhub;

public class Podcast extends Song{
    private String description;

    public Podcast(String id, String name, String genre, double duration, int year, double rate, String lyrics, String description) {
        super(id, name, genre, duration, year, rate, lyrics);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "description='" + description + '\'' +
                "} " + super.toString();
    }
}
