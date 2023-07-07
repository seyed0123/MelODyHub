package com.example.melodyhub;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class Podcast extends Song implements Serializable {
    private String description;

    public Podcast(String id, String name, String genre, double duration, int year, double rate, String lyrics,String path, String description) {
        super(id, name, genre, duration, year, rate, lyrics,path);
        this.description = description;
    }
    @JsonCreator
    public static Podcast createFromJson(@JsonProperty("id") String id,
                                         @JsonProperty("name") String name,
                                         @JsonProperty("genre") String genre,
                                         @JsonProperty("duration") double duration,
                                         @JsonProperty("year") int year,
                                         @JsonProperty("rate") double rate,
                                         @JsonProperty("lyrics") String lyrics,
                                         @JsonProperty("path") String path,
                                         @JsonProperty("description") String description) {
        return new Podcast(id, name, genre, duration, year, rate, lyrics,path, description);
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
