package com.example.melodyhub.Server.MelodyHub;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Account {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String Image;

    public Account(String username, String password, String email, String phoneNumber, String image) {
        id = UUID.randomUUID();
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        Image = image;
    }
    private String hashPassword(String password)
    {
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }
    private boolean checkPassword(String plain,String hashed)
    {
        return BCrypt.checkpw(plain,hashed);
    }
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
        MelodyHub.sendQuery("update person set pass = '"+this.password+"' where id = '"+id+"';");

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        MelodyHub.sendQuery("update person set email = '"+email+"' where id = '"+id+"';");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        MelodyHub.sendQuery("update person set phone = '"+phoneNumber+"' where id = '"+id+"';");
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
        MelodyHub.sendQuery("update person set image = '"+image+"' where id = '"+id+"';");
    }

    public ArrayList<UUID> getFollowings()
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select user1id from follow where user2id='"+getId()+"';");
        while (true) {
            try {
                if (!res.next()) break;
                ret.add(UUID.fromString(res.getString("user1id")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }

    public ArrayList<UUID> getFollowers()
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select user2id from follow where user1id='"+getId()+"';");
        while (true) {
            try {
                if (!res.next()) break;
                ret.add(UUID.fromString(res.getString("user2id")));
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }

    public void follow(UUID user)
    {
        MelodyHub.sendQuery("insert into follow (user1id, user2id) VALUES (user1id = '"+getId()+"' , user2id ='"+user+"');");
    }

    public void unfollow(UUID user) {
        MelodyHub.sendQuery("delete from follow where user1id ='"+getId()+"' and user2id='"+user+"';");
    }
    public void addPlaylist(UUID playlist)
    {
        MelodyHub.sendQuery("insert into playlist_owning (playlistid, ownerid) values (playlistid = '"+playlist+"' , ownerid = '"+id+"');");
    }

    public void removePlaylist(UUID playlist) throws Exception {
        MelodyHub.sendQuery("delete from playlist_owning where ownerid='"+getId()+"' and playlistid ='"+playlist+"';");
    }

    public ArrayList<UUID> getPlaylists()
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery("select playlistid from playlist_owning where ownerid ='"+getId()+"';");
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
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
