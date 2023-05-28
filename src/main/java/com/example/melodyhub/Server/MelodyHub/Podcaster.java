package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Podcaster extends Account{
    private boolean verify;
    private String bio;

    public Podcaster(String username, String password, String email, String phoneNumber, String image, boolean verify, String bio) {
        super(username, password, email, phoneNumber, image);
        this.bio=bio;
        this.verify=verify;
    }

    public boolean isVerify() {
        return verify;
        /*ResultSet res = MelodyHub.sendQuery("select verify from podcaster where id='"+getId()+"';");
        try {
            return res.getBoolean("verify");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void setVerify(boolean verify) {
        this.verify=verify;
        MelodyHub.sendQuery("update podcaster set verify = "+verify+" where id ='"+getId()+"';");
    }

    public String getBio() {
        return bio;
        /*ResultSet res = MelodyHub.sendQuery("select bio from podcaster where id='"+getId()+"';");
        try {
            return res.getString("bio");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void setBio(String bio) {
        this.bio=bio;
        MelodyHub.sendQuery("update artist set bio = '"+bio+"' where id ='"+getId()+"';");
    }

    public ArrayList<UUID> getPodcasts() {
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

    public void addPodcast(UUID podcast) {
        MelodyHub.sendQuery("insert into song_artist (songid, artistid) VALUES (songid = '"+podcast+"' , artistid = '"+getId()+"');");    }

    public void removePodcast(UUID podcast) {
        MelodyHub.sendQuery("delete from song_artist where artistid = '"+getId()+"' and songid = '"+podcast+"';");
    }
    @Override
    public String toString() {
        return "Podcast{" +
                "verify=" + isVerify() +
                ", bio='" + getBio() + '\'' +
                "} " + super.toString();
    }
}
