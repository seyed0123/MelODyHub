package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SongPerform {
    public static ArrayList<String> songGenre(String genre)
    {
        ArrayList<String> ret = new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery("select id from song where genre='"+genre+"';");
        if(res==null)
            return ret;
        while (true) {
            try {
                ret.add((res.getString("id")));
                if (!res.next()) break;
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }
    public static ArrayList<String> getArtist(String id)
    {
        ArrayList<String> ret = new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery(String.format("select artistid from song_artist where songid='%s';",id));
        if (res == null){
            return ret;
        }
        while (true) {
            try {
                if (!res.next()) break;
                ret.add((res.getString("artistid")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }
    public static ArrayList<String> commentOfSong(String id)
    {
        ArrayList<String> ret = new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery(String.format("select comment from comment where songid='%s';",id));
        while (true) {
            try {
                if (!res.next()) break;
                ret.add((res.getString("comment")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }
    public static ArrayList<String> popularSong()
    {
        ArrayList<String> ret = new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery(String.format("select id from song order by rate DESC limit 15;"));
        if(res==null)
            return ret;
        while (true) {
            try {
                ret.add((res.getString("id")));
                if (!res.next()) break;
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }
}
