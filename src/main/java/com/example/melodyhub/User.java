package com.example.melodyhub;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.UUID;

public class User extends Account implements Serializable {
    private ArrayList<String> queue;
    private String imageStory;
    private String gender;
    private Date age;
    private Song currentPlay;
    private boolean premium;
    private  ArrayList<String> oldNotification;
    private  ArrayList<String> notification;

    public User(String uuid, String username, String password, String email, String phoneNumber, String image, ArrayList<String> queue, String imageStory, String gender, Date age, Song currentPlay, boolean premium, ArrayList<String> oldNotification, ArrayList<String> notification) {
        super(uuid, username, password, email, phoneNumber, image);
        this.queue = queue;
        this.imageStory = imageStory;
        this.gender = gender;
        this.age = age;
        this.currentPlay = currentPlay;
        this.premium = premium;
        this.oldNotification = oldNotification;
        this.notification = notification;
    }
    @JsonCreator
    public static User createFromJson(@JsonProperty("id") String id,
                                      @JsonProperty("username") String username,
                                      @JsonProperty("password") String password,
                                      @JsonProperty("email") String email,
                                      @JsonProperty("phoneNumber") String phoneNumber,
                                      @JsonProperty("image") String image,
                                      @JsonProperty("queue") ArrayList<String> queue,
                                      @JsonProperty("imageStory") String imageStory,
                                      @JsonProperty("gender") String gender,
                                      @JsonProperty("age") Date age,
                                      @JsonProperty("currentPlay") Song currentPlay,
                                      @JsonProperty("premium") boolean premium,
                                      @JsonProperty("oldNotification") ArrayList<String> oldNotification,
                                      @JsonProperty("notification") ArrayList<String> notification) {
        return new User(id, username, password, email, phoneNumber, image, queue, imageStory, gender, age, currentPlay, premium, oldNotification, notification);
    }
    public User(String uuid, String username, String password, String email, String phoneNumber, String image) {
        super(uuid, username, password, email, phoneNumber, image);
    }

    public void setOldNotification(ArrayList<String> oldNotification) {
        this.oldNotification = oldNotification;
    }

    public void setNotification(ArrayList<String> notification) {
        this.notification = notification;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public ArrayList<String> getQueue() {
        return queue;
    }

    public void addQueue(String song)
    {
        if(queue==null)
            queue = new ArrayList<>();
        queue.add(song);
    }

    public void setQueue(ArrayList<String> queue) {
        this.queue = queue;
    }

    public void removeQueue(UUID song) {
        queue.remove(song);
    }
    public void setAge(Date age)
    {
        this.age=age;
    }

    public Date getAge()
    {
        return age;
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

    public void addNotification(String notification)
    {
        if(this.notification==null)
           this.notification= new ArrayList<>();
        this.notification.add(notification);
    }

    public ArrayList<String> getOldNotification() {
        return oldNotification;
    }

    public ArrayList<String> getNotification() {
        return notification;
    }

    public void removeNotif(String notif)
    {
        if(notification.contains(notif))
            notification.remove(notif);
    }
    @Override
    public String toString() {
        return "User{" +
                "currentPlay=" + currentPlay +
                "} " + super.toString();
    }
}
