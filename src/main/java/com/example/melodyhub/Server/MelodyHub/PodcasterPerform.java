package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class PodcasterPerform extends AccountPerform{
    public static ArrayList<String> getPodcasts(UUID Id) {
        ArrayList<String> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select songid from song_artist where artistid = '"+Id+"';");
        if(res==null)
        {
            return null;
        }
        while (true) {
            try {
                if (!res.next()) break;
                ret.add((res.getString("songid")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }

    public static void addPodcast(UUID Id,String podcast) {
        MelodyHub.sendQuery("insert into song_artist (songid, artistid) VALUES (songid = '"+podcast+"' , artistid = '"+Id+"');");    }

    public static void removePodcast(UUID Id,UUID podcast) {
        MelodyHub.sendQuery("delete from song_artist where artistid = '"+Id+"' and songid = '"+podcast+"';");
    }
}
