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
    }

    public static void changeSongOrder(UUID playlist,String song1,String song2) {
        MelodyHub.sendQuery("UPDATE Song_Playlist\n" +
                "SET songOrder = CASE songId\n" +
                "                    WHEN '"+song1+"' THEN (SELECT songOrder FROM Song_Playlist WHERE songId = '"+song2+"' AND playlistId = 'playlist_id')\n" +
                "                    WHEN '"+song2+"' THEN (SELECT songOrder FROM Song_Playlist WHERE songId = '"+song1+"' AND playlistId = 'playlist_id')\n" +
                "    END\n" +
                "WHERE songId IN ('"+song1+"', '"+song2+"') AND playlistId = '"+playlist+"';");
    }

    public static void placesASong(UUID playlist,String song,int order)
    {
        MelodyHub.sendQuery("UPDATE Song_Playlist\n" +
                "SET songOrder = \n" +
                "    CASE\n" +
                "        WHEN songOrder = (\n" +
                "            SELECT songOrder\n" +
                "            FROM Song_Playlist\n" +
                "            WHERE songId = '"+song+"' AND playlistId = '"+playlist+"'\n" +
                "        ) THEN "+order+"\n" +
                "        WHEN songOrder BETWEEN "+order+" AND (\n" +
                "            SELECT songOrder\n" +
                "            FROM Song_Playlist\n" +
                "            WHERE songId = '"+song+"' AND playlistId = '"+playlist+"'\n" +
                "        ) - 1 THEN songOrder + 1\n" +
                "        ELSE songOrder\n" +
                "    END\n" +
                "WHERE playlistId = '"+playlist+"';");
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
