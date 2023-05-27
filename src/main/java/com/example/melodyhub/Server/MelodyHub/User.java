package com.example.melodyhub.Server.MelodyHub;

import java.util.ArrayList;
import java.util.UUID;

public class User extends Account{
    private ArrayList<UUID> queue;
    private String imageStory;
    private String gender;
    private Song currentPlay;
    private final ArrayList<String> oldNotification;
    private final ArrayList<String> notification;

    public User(String username, String password, String email, String phoneNumber, String image, ArrayList<UUID> queue, String imageStory,String gender, Song currentPlay, ArrayList<String> oldNotification,ArrayList<String> notification) {
        super(username, password, email, phoneNumber, image);
        this.gender=gender;
        this.queue = queue;
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

    }

    public ArrayList<UUID> getRecommend()
    {
        return null;
    }

    public void addFavouritePlaylist(UUID playlist)
    {

    }

    public void removeFavoritePlaylist(UUID playlist)
    {

    }
    public ArrayList<UUID> getFavorite()
    {
        return null;
    }

    public void likeSong(UUID song)
    {

    }

    public void dislikeSong(UUID song) throws Exception {
        try
        {

        }catch (Exception e) {
            throw new Exception();
        }
    }

    public void addHistory()
    {

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

    public String getImageStory() {
        return imageStory;
    }

    public void setImageStory(String imageStory) {
        this.imageStory = imageStory;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Song getCurrentPlay() {
        return currentPlay;
    }

    public void setCurrentPlay(Song currentPlay) {
        this.currentPlay = currentPlay;
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
    @Override
    public String toString() {
        return "User{" +
                "currentPlay=" + currentPlay +
                "} " + super.toString();
    }
}
