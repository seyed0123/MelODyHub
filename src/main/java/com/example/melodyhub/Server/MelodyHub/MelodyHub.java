package com.example.melodyhub.Server.MelodyHub;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class MelodyHub {
    private final Account account;
    public static Connection connection;
    public static Gson gson;

    public MelodyHub(Account account) {
        this.account = account;
    }

    public static synchronized ResultSet sendQuery(String query) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean login(String username, String password) {
        return false;
    }

    public User findUser(UUID user)
    {
        return null;
    }

    public Artist findArtist(UUID Artist)
    {
        return null;
    }

    public Podcaster findPodcaster(UUID Podcaster)
    {
        return null;
    }
    public Song findSong(UUID song)
    {
        return null;
    }

    public PlayList findPlaylist(UUID playlist)
    {
        return null;
    }

    public Podcast findPodcast(UUID podcast)
    {
        return null;
    }

    public void createPlaylist(PlayList playList)
    {

    }

    public void removePlaylist(UUID playlist)
    {

    }

    public void createSong(Song song)
    {

    }

    public void removeSong(UUID song)
    {

    }

    public void createUser(User user)
    {

    }

    public void removeUser(UUID user)
    {

    }

    public void updateUser(UUID user)
    {

    }

    public void createArtist(Artist Artist)
    {

    }

    public void removeArtist(UUID Artist)
    {

    }

    public void updateArtist(UUID Artist)
    {

    }

    public void createPodcaster(Podcaster Podcaster)
    {

    }

    public void removePodcaster(UUID Podcaster)
    {

    }


    public void updatePodcaster(UUID Podcaster)
    {

    }

    public void downloadSong(UUID song)
    {

    }
    public ArrayList<Object> search(String searched)
    {
        return null;
    }
    public void logout()
    {

    }

}
