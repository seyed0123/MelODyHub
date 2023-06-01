package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ArtistPerform extends AccountPerform{
    public static ArrayList<UUID> getSongs(UUID Id) {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select songid from song_artist where artistid = '"+Id+"';");
        if(res==null)
        {
            return null;
        }
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

    public static void addSong(UUID Id,UUID song) {
        MelodyHub.sendQuery("insert into song_artist (songid, artistid) VALUES (songid = '"+song+"' , artistid = '"+Id+"');");
    }

    public static void removeSong(UUID Id,UUID song) {
        MelodyHub.sendQuery("delete from song_artist where artistid = '"+Id+"' and songid = '"+song+"';");
    }
}
