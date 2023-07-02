package com.example.melodyhub.Server.MelodyHub;

import com.example.melodyhub.Song;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class PlaylistPerform {
    public static ArrayList<String> getSongs(UUID id) {
        ArrayList<String> ret=new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery("select songid from song_playlist where playlistid='"+id+"' ORDER BY SongOrder ASC;");
        if(res==null)
            return ret;
        while (true) {
            try {
                ret.add((res.getString("songid")));
                if (!res.next()) break;
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }

    public static void addSong(UUID id,String song) {
        MelodyHub.sendQuery("INSERT INTO Song_Playlist (songId, playlistId, songOrder)\n" +
                "SELECT '"+song+"', '"+id+"', COALESCE(MAX(songOrder), 0) + 1\n" +
                "FROM Song_Playlist\n" +
                "WHERE playlistId = '"+id+"';");
        MelodyHub.sendQuery("update playlists set duration = duration + "+MelodyHub.findSong(song).getDuration()+" where id = '"+id+"';");
    }

    public static void removeSong(UUID id,String song) {
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
        MelodyHub.sendQuery("update playlists set duration = duration - "+MelodyHub.findSong(song).getDuration()+" where id = '"+id+"';");
    }

    public static void changeSongOrder(UUID playlist,String song1,String song2) {
        MelodyHub.sendQuery("BEGIN;\n" +
                "\n" +
                "-- Get the current order of song1 and store it in a CTE\n" +
                "WITH temp_order AS (\n" +
                "    SELECT songOrder FROM Song_Playlist\n" +
                "    WHERE songId = '"+song1+"' AND playlistId = '"+playlist+"'\n" +
                ")\n" +
                "-- Update the order of both songs\n" +
                "UPDATE Song_Playlist\n" +
                "SET songOrder = CASE songId\n" +
                "                    WHEN '"+song1+"' THEN (SELECT songOrder FROM Song_Playlist WHERE songId = '"+song2+"' AND playlistId = '"+playlist+"')\n" +
                "                    WHEN '"+song2+"' THEN (SELECT songOrder FROM temp_order)\n" +
                "    END\n" +
                "WHERE songId IN ('"+song1+"', '"+song2+"') AND playlistId = '"+playlist+"';\n" +
                "\n" +
                "COMMIT;");
    }

    public static void placesASong(UUID playlist,String song,int order)
    {
        MelodyHub.sendQuery("WITH old_order AS (\n" +
                "    SELECT songorder\n" +
                "    FROM song_playlist\n" +
                "    WHERE songid = '"+song+"' AND playlistid = '"+playlist+"'\n" +
                "),\n" +
                "     new_order AS (\n" +
                "         SELECT "+order+" AS songorder\n" +
                "     )\n" +
                "UPDATE song_playlist\n" +
                "SET songorder = CASE\n" +
                "                    WHEN old_order.songorder < new_order.songorder THEN songorder - 1\n" +
                "                    ELSE songorder + 1\n" +
                "    END\n" +
                "FROM old_order, new_order\n" +
                "WHERE playlistid = '"+playlist+"'\n" +
                "  AND songorder IN (old_order.songorder + 1, new_order.songorder, old_order.songorder - 1);\n" +
                "with new_order AS (\n" +
                "         SELECT "+order+" AS songorder\n" +
                "     )\n" +
                "UPDATE song_playlist\n" +
                "SET songorder = new_order.songorder\n" +
                "FROM new_order\n" +
                "WHERE songid = '"+song+"' AND playlistid = '"+playlist+"';");
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
    public static UUID firstOwner(UUID playlist)
    {
        ResultSet res = MelodyHub.sendQuery("select first_owner from playlists where id ='"+playlist+"';");
        UUID first =null;
        try {
            first = UUID.fromString(res.getString("first_owner"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return first;
    }
    public static ArrayList<String> smartShuffle(UUID playlist)
    {
        ArrayList<String> songs = getSongs(playlist);
        ArrayList<String> shuffled = new ArrayList<>();
        String lastGenre = null;
        while (!songs.isEmpty()) {
            ArrayList<Song> filtered = new ArrayList<>();
            for (String song : songs) {
                Song songObj = MelodyHub.findSong(song);
                String genre = songObj.getGenre();
                if (!genre.equals(lastGenre)) {
                    filtered.add(songObj);
                }
            }

            Collections.shuffle(filtered);
            if (!filtered.isEmpty()) {
                Song song = filtered.get(0);
                shuffled.add(song.getId());
                lastGenre = song.getGenre();
                songs.remove(song.getId());
            } else {
                lastGenre = null;
                Collections.shuffle(songs);
            }
        }
        return shuffled;
    }
}
