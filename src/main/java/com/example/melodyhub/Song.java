package com.example.melodyhub;

import java.io.Serializable;
import java.util.UUID;

public class Song implements Serializable {
    private UUID id;
    private String name;
    private String genre;
    private double duration;
    private int year;
    private double rate;
    private String lyrics;

    public Song(String id, String name, String genre, double duration, int year, double rate, String lyrics) {
        this.id = UUID.fromString(id);
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.year = year;
        this.rate = rate;
        this.lyrics = lyrics;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", year=" + year +
                ", rate=" + rate +
                '}';
    }
}
