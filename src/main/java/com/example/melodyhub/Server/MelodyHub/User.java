package com.example.melodyhub.Server.MelodyHub;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class User extends Account{
    private ArrayList<UUID> queue;
    private String imageStory;
    private String gender;
    private int age;
    private Song currentPlay;
    private final ArrayList<String> oldNotification;
    private final ArrayList<String> notification;

    public User(String username, String password, String email, String phoneNumber, String image, ArrayList<UUID> queue, String imageStory,String gender,int age, Song currentPlay, ArrayList<String> oldNotification,ArrayList<String> notification) {
        super(username, password, email, phoneNumber, image);
        this.gender=gender;
        this.queue = queue;
        this.age= age;
        this.imageStory = imageStory;
        this.currentPlay = currentPlay;
        this.oldNotification = oldNotification;
        this.notification = notification;
    }
    public void shareSong(UUID song,UUID user)
    {

    }

    public void sharePlaylist(UUID playlist, UUID user)
    {
        MelodyHub.sendQuery("Insert into playlist_owning (playlistid, ownerid) VALUES (playlistid = '"+playlist+"' , ownerid = '"+user+"');");
    }

    public ArrayList<UUID> getRecommend()
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("SELECT  songid FROM recommend_song where userid = '"+getId()+"';");
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

    public void addFavouritePlaylist(UUID playlist)
    {
        MelodyHub.sendQuery("INSERT INTO favorite_playlists (playlistid, userid) VALUES (playlistid = '"+playlist+"' , userid = '"+getId()+"');");
    }

    public void removeFavoritePlaylist(UUID playlist)
    {
        MelodyHub.sendQuery("DELETE from favorite_playlists where playlistid = '"+playlist+"';");
    }
    public ArrayList<UUID> getFavorite()
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("SELECT playlistid FROM favorite_playlists WHERE userid='"+getId()+"';");
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

    public void likeSong(UUID song)
    {
        MelodyHub.sendQuery("INSERT INTO liked_songs (songid, userid) VALUES (songid ='"+song+"', userid='"+getId()+"');");
    }

    public void dislikeSong(UUID song) throws Exception {
        try
        {
            MelodyHub.sendQuery("DELETE FROM liked_songs WHERE songid ='"+song+"' AND userid = '"+getId()+"';");
        }catch (Exception e) {
            throw new Exception();
        }
    }

    public void addHistory(String command)
    {
        MelodyHub.sendQuery("INSERT INTO history (userid, command) VALUES (userid = '"+getId()+"' , command = '"+command+"');");
    }

    public ArrayList<UUID> getQueue() {
        return queue;
    }

    public void addQueue(UUID song)
    {
        queue.add(song);
    }

    public void setQueue(ArrayList<UUID> queue) {
        this.queue = queue;
    }

    public void removeQueue(UUID song) throws Exception {
        try{
            queue.remove(song);
        }catch (Exception e) {
            throw new Exception();
        }
    }
    public void setAge(int age)
    {
        this.age=age;
        MelodyHub.sendQuery("update person set age = "+age+" where id = '"+getId()+"';");
    }

    public int getAge()
    {
        return age;
        /*ResultSet resultSet = MelodyHub.sendQuery("select age from person where id='"+getId()+"';");
        try {
            return resultSet.getInt("age");
        } catch (SQLException e) {
            return 0;
        }*/
    }

    public String getImageStory() {
        return imageStory;
    }

    public void setImageStory(String imageStory) {
        this.imageStory = imageStory;
        MelodyHub.sendQuery("update person set image_story = '"+imageStory+"' where id = '"+getId()+"';");
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        MelodyHub.sendQuery("update person set gender = '"+gender+"' where id = '"+getId()+"';");
    }

    public Song getCurrentPlay() {
        return currentPlay;
    }

    public void setCurrentPlay(Song currentPlay) {
        this.currentPlay = currentPlay;
    }

    public void addNotification(String notification)
    {
        this.notification.add(notification);
    }

    public ArrayList<String> getOldNotification() {
        return oldNotification;
    }

    public ArrayList<String> getNotification() {
        return notification;
    }
    public String seeNotification()
    {
        if(notification.size()==0)
            return null;
        String notif = notification.get(0);
        oldNotification.add(notif);
        notification.remove(0);
        return notif;

    }
    public void save()
    {
        String notification = MelodyHub.gson.toJson(this.notification);
        String oldNotification = MelodyHub.gson.toJson(this.oldNotification);
        MelodyHub.sendQuery("update person set image = notifications = '"+notification+"' ,old_notification='"+oldNotification+"' where id = '"+getId()+"';");
    }
    @Override
    public String toString() {
        return "User{" +
                "currentPlay=" + currentPlay +
                "} " + super.toString();
    }
}
