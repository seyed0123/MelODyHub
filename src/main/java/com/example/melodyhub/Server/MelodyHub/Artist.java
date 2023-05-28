package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Artist extends Account{
    private String bio;
    private boolean verify;

    public Artist(String username, String password, String email, String phoneNumber, String image, boolean verify, String bio) {
        super(username, password, email, phoneNumber, image);
        this.bio=bio;
        this.verify=verify;
    }

    public boolean isVerify() {
        return verify;
        /*ResultSet res = MelodyHub.sendQuery("select verify from artist where id='"+getId()+"';");
        try {
            return res.getBoolean("verify");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void setVerify(boolean verify) {
        MelodyHub.sendQuery("update artist set verify = "+verify+" where id ='"+getId()+"';");
        this.verify=verify;
    }

    public String getBio() {
        return this.bio;
        /*ResultSet res = MelodyHub.sendQuery("select bio from artist where id='"+getId()+"';");
        try {
            return res.getString("bio");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void setBio(String bio) {
        MelodyHub.sendQuery("update artist set bio = '"+bio+"' where id ='"+getId()+"';");
        this.bio=bio;
    }

    public ArrayList<UUID> getSongs() {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select songid from song_artist where artistid = '"+getId()+"';");
        while (true) {
            try {
                if (!res.next()) break;
                ret.add(UUID.fromString(res.getString("songid")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }

    public void addSong(UUID song) {
        MelodyHub.sendQuery("insert into song_artist (songid, artistid) VALUES (songid = '"+song+"' , artistid = '"+getId()+"');");
    }

    public void removeSong(UUID song) {
        MelodyHub.sendQuery("delete from song_artist where artistid = '"+getId()+"' and songid = '"+song+"';");
    }

    @Override
    public String toString() {
        return "Artist{" +
                "verify=" + isVerify() +
                ", bio='" + getBio() + '\'' +
                "} " + super.toString();
    }
}
