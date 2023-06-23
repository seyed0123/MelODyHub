package com.example.melodyhub.Server.MelodyHub;

import com.example.melodyhub.Song;
import com.google.gson.reflect.TypeToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.example.melodyhub.Server.MelodyHub.MelodyHub.*;

public class UserPerform extends AccountPerform {

    public static String shareSong(String song) {
        Random rand = new Random();
        String body = "";
        Song song1 = MelodyHub.findSong(song);
        int randomNumber = rand.nextInt(3);
        if (randomNumber == 0) {
            body = "Hey, I heard this amazing song the other day and " +
                    "I think you would really like it. It's got a great beat and the lyrics " +
                    "are so relatable. It's called " + song1.getName() + ".";
        } else if (randomNumber == 1) {
            body = "I think you'll love this song I heard. It's got a really unique sound and " +
                    "the vocals are just beautiful. " +
                    "The song is called " + song1.getName() + ".";
        } else {
            body = "I came across this song that I think you'll enjoy." +
                    " It's got a really catchy chorus and the melody is super uplifting." +
                    " It's called " + song1.getName() + " .";
        }
        return body;
    }

    public static void setAge(UUID user, Date age) {
        MelodyHub.sendQuery("update person set age = Cast('" + age + "' as date) where id = '" + user + "';");
    }
    public static void sharePlaylist(UUID playlist, UUID user) {
        MelodyHub.sendQuery("Insert into playlist_owning (playlistid, ownerid) VALUES ('" + playlist + "' , '" + user + "');");
    }

    public static ArrayList<String> getRecommend(UUID Id) {
        ArrayList<String> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("SELECT  songid FROM recommend_song where userid = '" + Id + "' limit 10;");
        if(res == null)
        {
            return ret;
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

    public static void addFavouritePlaylist(UUID Id, UUID playlist) {
        MelodyHub.sendQuery("INSERT INTO favorite_playlists (playlistid, userid) VALUES (playlistid = '" + playlist + "' , userid = '" + Id + "');");
    }

    public static void removeFavoritePlaylist(UUID id, UUID playlist) {
        MelodyHub.sendQuery("DELETE from favorite_playlists where playlistid = '" + playlist + "' AND userid = '" + id + "';");
    }

    public static ArrayList<UUID> getFavoritePlaylist(UUID Id) {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("SELECT playlistid FROM favorite_playlists WHERE userid='" + Id + "';");
        if (res == null) {
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

    public static void likeSong(UUID Id, String  song) {
        MelodyHub.sendQuery("INSERT INTO liked_songs (songid, userid) VALUES (songid ='" + song + "', userid='" + Id + "');");
    }

    public static void dislikeSong(UUID Id, String song) {
        MelodyHub.sendQuery("DELETE FROM liked_songs WHERE songid ='" + song + "' AND userid = '" + Id + "';");
    }

    public static ArrayList<UUID> getLikedSongs(UUID id) {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select songid from liked_songs where userid= '" + id + "';");
        if (res == null) {
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

    public static void addHistory(UUID Id, String type, String command) {
        MelodyHub.sendQuery("INSERT INTO history (userid, type , command) VALUES ('" + Id + "' , '" + type + "', '" + command + "');");
    }

    public static ArrayList<String> seeHistory(UUID id, String type) {
        ArrayList<String> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select command from history where userid='" + id + "' and type = '" + type + "';");
        if (res == null) {
            return ret;
        }
        while (true) {
            try {
                ret.add((res.getString("command")));
                if (!res.next()) break;
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }

    public static void save(UUID Id, ArrayList<String> notificationI, ArrayList<String> oldNotificationI, ArrayList<UUID> queueI) {
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
    public static void comment(String song,UUID user, String text)
    {
        MelodyHub.sendQuery("INSERT INTO comment (songid, userid, comment)\n" +
                "VALUES ('"+song+"', '"+user+"', '"+text+"')\n" +
                "ON CONFLICT (songid,userid)  DO UPDATE SET comment = 'new_value3';");
    }
}
