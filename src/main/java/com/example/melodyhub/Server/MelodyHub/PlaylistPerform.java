package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class PlaylistPerform {
    public static ArrayList<UUID> getSongs(UUID id) {
        ArrayList<UUID> ret=new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery("select songid from song_playlist where playlistid='"+id+"';");
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

    public static void addSong(UUID id,UUID song) {
        MelodyHub.sendQuery("INSERT INTO Song_Playlist (songId, playlistId, songOrder)\n" +
                "SELECT '"+song+"', '"+id+"', COALESCE(MAX(songOrder), 0) + 1\n" +
                "FROM Song_Playlist\n" +
                "WHERE playlistId = '"+id+"';");
    }

    public static void removeSong(UUID id,UUID song) {
        MelodyHub.sendQuery("UPDATE Song_Playlist\n" +
                "SET songOrder = songOrder - 1\n" +
                "WHERE playlistId = '"+id+"' AND songOrder > (\n" +
                "    SELECT songOrder\n" +
                "    FROM Song_Playlist\n" +
                "    WHERE songId = '"+song+"' AND playlistId = '"+id+"'\n" +
                ");\n" +
                "\n" +
                "DELETE FROM Song_Playlist\n" +
                "WHERE songId = '"+song+"' AND playlistId = '"+id+"';");
    }

    public static void changeSongOrder(UUID song1,UUID song2) {
        MelodyHub.sendQuery("UPDATE Song_Playlist\n" +
                "SET songOrder = CASE songId\n" +
                "                    WHEN '"+song1+"' THEN (SELECT songOrder FROM Song_Playlist WHERE songId = '"+song2+"' AND playlistId = 'playlist_id')\n" +
                "                    WHEN '"+song2+"' THEN (SELECT songOrder FROM Song_Playlist WHERE songId = '"+song1+"' AND playlistId = 'playlist_id')\n" +
                "    END\n" +
                "WHERE songId IN ('"+song1+"', '"+song2+"') AND playlistId = 'playlist_id';");
    }

    public static ArrayList<UUID> getOwners(UUID id) {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select ownerid from playlist_owning where playlistid='"+id+"';");
        while (true) {
            try {
                if (!res.next()) break;
                ret.add(UUID.fromString(res.getString("ownerid")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;

    }

    public static void addOwner(UUID id,UUID user) {
        MelodyHub.sendQuery(String.format("insert into playlist_owning (playlistid, ownerid) VALUES (playlistid='%s',ownerid='%s');",id,user));
    }

    public static void removeOwner(UUID id,UUID user) {
        MelodyHub.sendQuery(String.format("delete from playlist_owning where playlistid='%s' and ownerid='%s';",id,user));
    }
}
