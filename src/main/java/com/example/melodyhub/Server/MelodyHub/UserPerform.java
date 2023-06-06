package com.example.melodyhub.Server.MelodyHub;

import com.google.gson.reflect.TypeToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.example.melodyhub.Server.MelodyHub.MelodyHub.*;

public class UserPerform extends AccountPerform{

    public static String shareSong(UUID song, UUID user)
    {
        String body="";
        return body;
    }
    public static void setAge(UUID user,Date age)
    {
        MelodyHub.sendQuery("update person set age = Cast('"+age+"' as date) where id = '"+user+"';");
    }
    public static void sharePlaylist(UUID playlist, UUID user)
    {
        MelodyHub.sendQuery("Insert into playlist_owning (playlistid, ownerid) VALUES (playlistid = '"+playlist+"' , ownerid = '"+user+"');");
    }

    public static ArrayList<UUID> getRecommend(UUID Id)
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("SELECT  songid FROM recommend_song where userid = '"+Id+"';");
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

    public static void addFavouritePlaylist(UUID Id,UUID playlist)
    {
        MelodyHub.sendQuery("INSERT INTO favorite_playlists (playlistid, userid) VALUES (playlistid = '"+playlist+"' , userid = '"+Id+"');");
    }

    public static void removeFavoritePlaylist(UUID playlist)
    {
        MelodyHub.sendQuery("DELETE from favorite_playlists where playlistid = '"+playlist+"';");
    }
    public static ArrayList<UUID> getFavorite(UUID Id)
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("SELECT playlistid FROM favorite_playlists WHERE userid='"+Id+"';");
        if(res==null)
        {
            return null;
        }
        while (true) {
            try {
                if (!res.next()) break;
                ret.add(UUID.fromString(res.getString("playlistid")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }

    public static void likeSong(UUID Id,UUID song)
    {
        MelodyHub.sendQuery("INSERT INTO liked_songs (songid, userid) VALUES (songid ='"+song+"', userid='"+Id+"');");
    }

    public static void dislikeSong(UUID Id,UUID song) {
        MelodyHub.sendQuery("DELETE FROM liked_songs WHERE songid ='"+song+"' AND userid = '"+Id+"';");
    }

    public static void addHistory(UUID Id,String command)
    {
        MelodyHub.sendQuery("INSERT INTO history (userid, command) VALUES (userid = '"+Id+"' , command = '"+command+"');");
    }
    public static void save(UUID Id,ArrayList<String>notificationI,ArrayList<String>oldNotificationI,ArrayList<UUID>queueI)
    {
        String notification = gson.toJson(notificationI);
        String oldNotification = gson.toJson(oldNotificationI);
        String queue = gson.toJson(queueI);
        MelodyHub.sendQuery("update person set notifications = '"+notification+"' ,old_notification='"+oldNotification+"' , queue = '"+queue+"'where id = '"+Id+"';");
    }
    public static void addAnswer(UUID userId,int questionId,String answer)
    {
        MelodyHub.sendQuery(String.format("insert into answer (userid, questionid, answer) values ('%s','%d','%s');",userId,questionId,hashPassword(answer)));
    }
    public static boolean checkAnswer(UUID userId,int questionId,String answer)
    {
        ResultSet res = MelodyHub.sendQuery(String.format("select answer.answer from answer where userid='%s' and questionid='%d';",userId,questionId));
        try {
            return checkPassword(answer,res.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String> refreshNotification(UUID user)
    {
        ResultSet res = MelodyHub.sendQuery(String.format("select notifications from person where id = '%s';",user));
        try {
            return gson.fromJson(res.getString("notifications"), new TypeToken<ArrayList<String>>(){}.getType());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
