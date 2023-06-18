package com.example.melodyhub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Song implements Serializable {
    private String id;
    private String name;
    private String genre;
    private double duration;
    private int year;
    private double rate;
    private String lyrics;
    private String path;

    public Song(String id, String name, String genre, double duration, int year, double rate, String lyrics,String path) {
        this.id=id;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.year = year;
        this.rate = rate;
        this.lyrics = lyrics;
        this.path=path;
    }
    @JsonCreator
    public static Song createFromJson(@JsonProperty("id") String id,
                                      @JsonProperty("name") String name,
                                      @JsonProperty("genre") String genre,
                                      @JsonProperty("duration") double duration,
                                      @JsonProperty("year") int year,
                                      @JsonProperty("rate") double rate,
                                      @JsonProperty("path") String path,
                                      @JsonProperty("lyrics") String lyrics) {
        return new Song(id, name, genre, duration, year, rate, lyrics,path);
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(UUID id) {
        this.id = id.toString();
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
