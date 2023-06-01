package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SongPerform {
    public ArrayList<UUID> songGenre(String genre)
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery("select id from song where genre='"+genre+"';");
        while (true) {
            try {
                if (!res.next()) break;
                ret.add(UUID.fromString(res.getString("id")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }
    public ArrayList<UUID> getArtist(UUID id)
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery(String.format("select artistid from song_artist where songid='%s';",id));
        while (true) {
            try {
                if (!res.next()) break;
                ret.add(UUID.fromString(res.getString("artistid")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }
}
