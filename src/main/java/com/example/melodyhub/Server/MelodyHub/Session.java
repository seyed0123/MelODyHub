package com.example.melodyhub.Server.MelodyHub;

import com.example.melodyhub.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
public class Session implements Runnable{

    private Account account;
    private Socket socket;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private BufferedReader input ;
    private PrintWriter output ;
    private Socket loXdySocket;
    private BufferedReader loXdyInput ;
    private PrintWriter loXdyOutput ;
    private Cipher cipherEncrypt;
    private Cipher cipherDecrypt;

    private Gson gson;

    private ObjectMapper objectMapper;

    public Session(Socket socket,Socket loXdy) {
        this.loXdySocket= loXdy;
        this.socket = socket;
        try {
            objIn = new ObjectInputStream(socket.getInputStream());
            objOut = new ObjectOutputStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            loXdyInput = new BufferedReader(new InputStreamReader(loXdy.getInputStream()));
            loXdyOutput = new PrintWriter(loXdy.getOutputStream(), true);
            gson = new Gson();
            objectMapper = new ObjectMapper();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessage(String message)
    {
        try {
            byte[] encrypt = cipherEncrypt.doFinal(message.getBytes());
            String base64Data = Base64.getEncoder().encodeToString(encrypt);
            output.println(gson.toJson(base64Data));
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    private String getMessage()
    {
        try {
            String base64 = gson.fromJson(input.readLine(),String.class);
            byte[] decodedData = Base64.getDecoder().decode(base64);
            byte[] decrypted = cipherDecrypt.doFinal(decodedData);
            return new String(decrypted);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    private void setKey()
    {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            Cipher encryptCipher = Cipher.getInstance("RSA");

            RSAPublicKey publicKey = (RSAPublicKey) objIn.readObject();
            //objIn.close();
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);


            byte[] encrypted = encryptCipher.doFinal(secretKey.getEncoded());
            String base64Data = Base64.getEncoder().encodeToString(encrypted);
            output.println(gson.toJson(base64Data));

            cipherEncrypt = Cipher.getInstance("AES");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKey);
            cipherDecrypt = Cipher.getInstance("AES");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void run() {
        setKey();
        String job;
        int count=0;
        try {
            while (true)
            {
                if(count>10)
                {
                    sendMessage("your are kicked");
                    socket.close();
                }
                job = getMessage();
                if(job==null)
                    return;
                System.out.println(job+" -->"+socket.getLocalSocketAddress());count++;
                if(Objects.equals(job, "login user"))
                {
                    String username = getMessage();
                    String password = getMessage();
                    User user;
                    if((user=MelodyHub.userLogin(username,password))!=null)
                    {
                        if(checkTOTP(user.getEmail())) {
                            account = user;
                            sendMessage("login OK");
                            sendMessage(objectMapper.writeValueAsString(user));
                            break;
                        }
                        else
                        {
                            sendMessage("code is wrong");
                        }
                    }else
                    {
                        sendMessage("login failed");
                    }
                }else if(Objects.equals(job, "login artist"))
                {
                    String username = getMessage();
                    String password = getMessage();
                    Artist artist;
                    if((artist=MelodyHub.artistLogin(username,password))!=null)
                    {
                        if(checkTOTP(artist.getEmail())) {
                            account = artist;
                            sendMessage("login OK");
                            sendMessage(objectMapper.writeValueAsString(artist));
                            break;
                        }
                        else
                        {
                            sendMessage("code is wrong");
                        }
                    }
                    sendMessage("login failed");
                }else if(Objects.equals(job, "login podcaster"))
                {
                    String username = getMessage();
                    String password = getMessage();
                    Podcaster podcaster;
                    if((podcaster=MelodyHub.podcasterLogin(username,password))!=null)
                    {
                        if(checkTOTP(podcaster.getEmail())) {
                            account = podcaster;
                            sendMessage("login OK");
                            sendMessage(objectMapper.writeValueAsString(podcaster));
                            break;
                        }else
                        {
                            sendMessage("code wrong");
                        }
                    }
                    sendMessage("login failed");
                }else if(Objects.equals(job, "create user"))
                {
                    JSONObject json = new JSONObject(getMessage());
                    String username = json.getString("username");
                    ResultSet res = MelodyHub.sendQuery("select * from person where username = '"+username+"';");
                    if(res==null)
                    {
                        String password = json.getString("password");
                        String email = json.getString("email");
                        String phoneNumber = json.getString("phone");
                        String gender = json.getString("gender");
                        String date = json.getString("date");
                        UUID id = UUID.randomUUID();
                        User user = new User(id.toString(),username,password,email,phoneNumber,"",new ArrayList<>(),"",gender
                                , Date.valueOf(date),null,false,new ArrayList<>(),new ArrayList<>());
                        MelodyHub.createUser(user);
                        int quesId = json.getInt("quesId");
                        String ans = json.getString("answer");
                        UserPerform.addAnswer(id,quesId,ans);
                        json.put("job",job);
                        MelodyHub.addLog(json);
                        sendMessage("done");
                    }else {
                        sendMessage("failed");
                    }
                }else if(job.equals("create artist"))
                {
                    JSONObject json = new JSONObject(getMessage());
                    String username = json.getString("username");
                    ResultSet res =MelodyHub.sendQuery("select * from artist where username = '"+username+"';");
                    if(res==null)
                    {
                        String password = json.getString("password");
                        String email = json.getString("email");
                        String phoneNumber = json.getString("phone");
                        UUID id = UUID.randomUUID();
                        Artist artist = new Artist(id.toString(),username,password,email,phoneNumber,"","",false,0,0,"");
                        MelodyHub.createArtist(artist);
                        int quesId = json.getInt("quesId");
                        String ans = json.getString("answer");
                        UserPerform.addAnswer(id,quesId,ans);
                        json.put("job",job);
                        MelodyHub.addLog(json);
                        sendMessage("done");
                    }else
                    {
                        sendMessage("failed");
                    }
                } else if (job.equals("create podcaster")) {
                    JSONObject json = new JSONObject(getMessage());
                    String username = json.getString("username");
                    ResultSet res =MelodyHub.sendQuery("select * from podcaster where username = '"+username+"';");
                    if(res==null)
                    {
                        String password = json.getString("password");
                        String email = json.getString("email");
                        String phoneNumber = json.getString("phone");
                        UUID id = UUID.randomUUID();
                        Podcaster podcaster = new Podcaster(id.toString(),username,password,email,phoneNumber,"",false,"",0);
                        MelodyHub.createPodcaster(podcaster);
                        int quesId = json.getInt("quesId");
                        String ans = json.getString("answer");
                        UserPerform.addAnswer(id,quesId,ans);
                        json.put("job",job);
                        MelodyHub.addLog(json);
                        sendMessage("done");
                    }else
                    {
                        sendMessage("failed");
                    }

                }else if (job.equals("forget pass")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String work = jsonObject.getString("work");
                    String username = jsonObject.getString("username");
                    String type = jsonObject.getString("type");
                    boolean flag = false;
                    UUID uuid;
                    if ((uuid = MelodyHub.findUserUsername(username)) == null && (uuid = MelodyHub.findArtistUsername(username)) == null && (uuid = MelodyHub.findPodcasterUsername(username)) == null) {
                        sendMessage("account not found");
                    }else if(Objects.equals(work, "TOTP"))
                    {
                        flag = checkTOTP(MelodyHub.findUser(uuid).getEmail());
                    } else if (Objects.equals(work, "answer")) {
                        String answer = jsonObject.getString("answer");
                        int quesN = jsonObject.getInt("number");
                        flag = UserPerform.checkAnswer(uuid,quesN,answer);
                    }
                    if(flag)
                    {
                        sendMessage("you are you");
                        String password = getMessage();
                        MelodyHub.updatePass(type,uuid,password);
                        sendMessage("password updated");
                    }else {
                        sendMessage("sorry you aren't you");
                    }
                }
            }
            while (true)
            {
                job = getMessage();
                if(job==null)
                    return;
                System.out.println(job+" -->"+socket.getLocalSocketAddress());
                if (job.equals("create playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String name = jsonObject.getString("name");
                    boolean personal = jsonObject.getBoolean("personal");
                    UUID artist=null;
                    if(!Objects.equals(jsonObject.getString("artist"), "null"))
                        artist = (MelodyHub.findArtistUsername(jsonObject.getString("artist")));
                    String artistName=null;
                    if(artist!=null)
                        artistName = artist.toString();
                    UUID firstOwner = MelodyHub.findUserUsername(jsonObject.getString("firstOwner"));
                    MelodyHub.createPlaylist(firstOwner,new PlayList(UUID.randomUUID().toString(),name,personal,0,0,artistName,firstOwner.toString()));
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("create song")) {
                    if((account instanceof Artist)) {
                        JSONObject jsonObject = new JSONObject(getMessage());
                        String name = jsonObject.getString("name");
                        String genre = jsonObject.getString("genre");
                        double duration = jsonObject.getDouble("duration");
                        int year = jsonObject.getInt("year");
                        double rate = jsonObject.getDouble("rate");
                        String lyrics = jsonObject.getString("lyrics");
                        String id = jsonObject.getString("id");

                        ArrayList<String> artists = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("artists");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String art = jsonArray.getString(i);
                            artists.add(art);
                        }
                        MelodyHub.createSong(new Song(id, name, genre, duration, year, rate, lyrics, null),artists);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                    }
                } else if (job.equals("update song")) {
                    if(!(account instanceof User)) {
                        JSONObject jsonObject = new JSONObject(getMessage());
                        UUID id = UUID.fromString(jsonObject.getString("id"));
                        HashMap<String, String> commands = objectMapper.readValue(jsonObject.getString("commands"), HashMap.class);
                        MelodyHub.updateSong(id, commands);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                    }
                } else if (job.equals("create podcast")) {
                    if(account instanceof Podcaster)
                    {
                        JSONObject jsonObject = new JSONObject(getMessage());
                        String name = jsonObject.getString("name");
                        String genre = jsonObject.getString("genre");
                        double duration = jsonObject.getDouble("duration");
                        int year = jsonObject.getInt("year");
                        double rate = jsonObject.getDouble("rate");
                        String lyrics = jsonObject.getString("lyrics");
                        String description = jsonObject.getString("des");
                        String id = jsonObject.getString("id");
                        MelodyHub.createPodcast(new Podcast(id, name, genre, duration, year, rate, lyrics, null,description),account.getId());
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                    }
                } else if (job.equals("update user")) {
                    if(account instanceof User)
                    {
                        UUID id = account.getId();
                        JSONObject jsonObject = new JSONObject(getMessage());
                        HashMap<String, String> commands = objectMapper.readValue(jsonObject.getString("command"), HashMap.class);
                        MelodyHub.updateUser(id, commands);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                    }
                } else if (job.equals("update premium")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String column = jsonObject.getString("column");
                    boolean per = jsonObject.getBoolean("premium");
                    if(account instanceof User)
                        MelodyHub.updatePremium("person",column,account.getId(),per);
                    else if (account instanceof Artist) {
                        MelodyHub.updatePremium("artist",column,account.getId(),per);
                    }else
                        MelodyHub.updatePremium("podcaster",column,account.getId(),per);
                } else if (job.equals("update pass")) {
                    if(checkTOTP(account.getEmail()))
                    {
                        sendMessage("new pass");
                        String pass = getMessage();
                        if(account instanceof User)
                            MelodyHub.updatePass("person",account.getId(),pass);
                        else if (account instanceof Artist) {
                            MelodyHub.updatePass("artist",account.getId(),pass);
                        }else
                            MelodyHub.updatePass("podcaster",account.getId(),pass);
                    }else {
                        sendMessage("verify failed");
                    }
                } else if (job.equals("update artist")) {
                    if(account instanceof Artist)
                    {
                        UUID id = account.getId();
                        JSONObject jsonObject = new JSONObject(getMessage());
                        HashMap<String,String> commands = objectMapper.readValue(jsonObject.getString("commads"),HashMap.class);
                        MelodyHub.updateArtist(id,commands);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                    }
                } else if (job.equals("update podcaster")) {
                    if(account instanceof Podcaster)
                    {
                        UUID id = account.getId();
                        JSONObject jsonObject = new JSONObject(getMessage());
                        HashMap<String,String> commands = objectMapper.readValue(jsonObject.getString("command"),HashMap.class);
                        MelodyHub.updatePodcaster(id,commands);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                    }
                } else if (job.equals("get user")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    sendMessage(objectMapper.writeValueAsString(MelodyHub.findUser(id)));
                } else if (job.equals("get artist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    sendMessage(objectMapper.writeValueAsString(MelodyHub.findArtist(id)));
                } else if (job.equals("get podcaster")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    sendMessage(objectMapper.writeValueAsString(MelodyHub.findPodcaster(id)));
                } else if (job.equals("get song")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String id = (jsonObject.getString("id"));
                    Song song = MelodyHub.findSong(id);
                    sendMessage(objectMapper.writeValueAsString(song));
                } else if (job.equals("get playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    PlayList playList = MelodyHub.findPlaylist(id);
                    sendMessage(objectMapper.writeValueAsString(playList));
                } else if (job.equals("search")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UserPerform.addHistory(account.getId(),"search",jsonObject.getString("searched"));
                    ArrayList<String> list= MelodyHub.search(jsonObject.getString("searched"));
                    sendMessage(objectMapper.writeValueAsString(list));
                    sendMessage(objectMapper.writeValueAsString(MelodyHub.searchArtist(jsonObject.getString("searched"))));
                    sendMessage(objectMapper.writeValueAsString(MelodyHub.searchUser(jsonObject.getString("searched"))));
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("get genre artist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    ArrayList<UUID> list = MelodyHub.getGenreArtist(jsonObject.getString("genre"));
                    sendMessage(objectMapper.writeValueAsString(list));
                } else if (job.equals("get followings")) {
                    ArrayList<UUID> followings = AccountPerform.getFollowings(account.getId());
                    sendMessage(objectMapper.writeValueAsString(followings));
                } else if (job.equals("get followers")) {
                    ArrayList<UUID> followers = AccountPerform.getFollowers(account.getId());
                    sendMessage(objectMapper.writeValueAsString(followers));
                } else if (job.equals("follow")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID followId = UUID.fromString(jsonObject.getString("followId"));
                    AccountPerform.follow(account.getId(),followId);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("unfollow")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID unfollow = UUID.fromString(jsonObject.getString("unfollowId"));
                    AccountPerform.unfollow(account.getId(),unfollow);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("remove playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    AccountPerform.removePlaylist(account.getId(),playlist);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("get playlists")) {
                    ArrayList<UUID> playlists = AccountPerform.getPlaylists(account.getId());
                    sendMessage(objectMapper.writeValueAsString(playlists));
                } else if (job.equals("get songs artist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID artist = UUID.fromString(jsonObject.getString("artist"));
                    ArrayList<String> songs = ArtistPerform.getSongs(artist);
                    sendMessage(objectMapper.writeValueAsString(songs));
                } else if (job.equals("get songs playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    ArrayList<String> songs = PlaylistPerform.getSongs(playlist);
                    sendMessage(objectMapper.writeValueAsString(songs));
                } else if (job.equals("add song playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    String song  = (jsonObject.getString("song"));
                    PlaylistPerform.addSong(playlist,song);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("remove song playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    String song = (jsonObject.getString("song"));
                    PlaylistPerform.removeSong(playlist,song);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("change song order playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    String song1 = (jsonObject.getString("song1"));
                    String song2 = (jsonObject.getString("song2"));
                    PlaylistPerform.changeSongOrder(playlist,song1,song2);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("get owners playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    ArrayList<UUID> owners = PlaylistPerform.getOwners(playlist);
                    if(owners.contains(account.getId()))
                    {
                        sendMessage(objectMapper.writeValueAsString(owners));
                    }else {
                        sendMessage(objectMapper.writeValueAsString(new ArrayList<String>()));
                    }
                } else if (job.equals("add owner")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    if(!(PlaylistPerform.firstOwner(playlist)==account.getId()))
                    {
                        sendMessage("don't have permission");
                    }else {
                        PlaylistPerform.addOwner(playlist,id);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                        sendMessage("done");
                    }
                } else if (job.equals("remove owner")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    if(!(PlaylistPerform.firstOwner(playlist)==account.getId()))
                    {
                        sendMessage("don't have permission");
                    }else {
                        PlaylistPerform.removeOwner(playlist,id);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                        sendMessage("done");
                    }
                } else if (job.equals("place a song in playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    String id = (jsonObject.getString("id"));
                    int order = Integer.parseInt(jsonObject.getString("order"));
                    PlaylistPerform.placesASong(playlist,id,order);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("smart shuffle")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    sendMessage(objectMapper.writeValueAsString(PlaylistPerform.smartShuffle(playlist)));
                } else if (job.equals("get podcast")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    sendMessage(objectMapper.writeValueAsString(PodcasterPerform.getPodcasts(id)));
                } else if (job.equals("find song genre")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String genre = jsonObject.getString("id");
                    sendMessage(objectMapper.writeValueAsString(SongPerform.songGenre(genre)));
                } else if (job.equals("get artist song")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String id = (jsonObject.getString("id"));
                    sendMessage(objectMapper.writeValueAsString(SongPerform.getArtist(id)));
                } else if (job.equals("share song")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String song = (jsonObject.getString("song"));
                    UUID user = (MelodyHub.findUserUsername(jsonObject.getString("user")));
                    if(account instanceof User)
                    {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("email",MelodyHub.findUser(user).getEmail());
                        jsonObject1.put("subject","Hi there,check this song");
                        jsonObject1.put("body",UserPerform.shareSong(song));
                        loXdyOutput.println("send email");
                        loXdyOutput.println(jsonObject1);
                    }
                } else if (job.equals("set age")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    Date date = objectMapper.readValue(jsonObject.getString("date"),Date.class);
                    UserPerform.setAge(account.getId(),date);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("share playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    UUID user = (MelodyHub.findUserUsername(jsonObject.getString("user")));
                    if((PlaylistPerform.firstOwner(playlist)==account.getId()))
                    {
                        sendMessage("don't have permission");
                    }else {
                        UserPerform.sharePlaylist(playlist,user);
                        jsonObject.put("job",job);
                        MelodyHub.addLog(jsonObject);
                        sendMessage("done");
                    }
                } else if (job.equals("get recommend")) {
                    sendMessage(objectMapper.writeValueAsString(UserPerform.getRecommend(account.getId())));
                }else if (job.equals("get popular")) {
                    sendMessage(objectMapper.writeValueAsString(SongPerform.popularSong()));
                } else if (job.equals("add favorite playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    UserPerform.addFavouritePlaylist(account.getId(),playlist);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("remove favorite playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID playlist = UUID.fromString(jsonObject.getString("playlist"));
                    UserPerform.removeFavoritePlaylist(account.getId(),playlist);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("get favorite playlist")) {
                    ArrayList<UUID> favorite = UserPerform.getFavoritePlaylist(account.getId());
                    sendMessage(objectMapper.writeValueAsString(favorite));
                } else if (job.equals("like song")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String song = (jsonObject.getString("song"));
                    UserPerform.likeSong(account.getId(),song);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("dislike song")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String song = (jsonObject.getString("song"));
                    UserPerform.dislikeSong(account.getId(),song);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("get liked songs")) {
                    ArrayList<UUID> songs = UserPerform.getLikedSongs(account.getId());
                    sendMessage(objectMapper.writeValueAsString(songs));
                } else if (job.equals("save user notif")) {
                    JSONObject inputs = new JSONObject(getMessage());
                    ArrayList<String> notif = objectMapper.readValue(inputs.getString("notif"),new TypeReference<ArrayList<String>>() {});
                    ArrayList<String> oldNotif = objectMapper.readValue(inputs.getString("oldNotif"),new TypeReference<ArrayList<String>>() {});
                    ArrayList<UUID> queue = objectMapper.readValue(inputs.getString("queue"),new TypeReference<ArrayList<UUID>>() {});
                    UserPerform.save(account.getId(),notif,oldNotif,queue);
                    inputs.put("job",job);
                    MelodyHub.addLog(inputs);
                } else if (job.equals("refresh notif")) {
                    ArrayList<String> notif = UserPerform.refreshNotification(account.getId());
                    sendMessage(objectMapper.writeValueAsString(notif));
                } else if (job.equals("see history")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String type = jsonObject.getString("type");
                    ArrayList<String> history = UserPerform.seeHistory(account.getId(),type);
                    sendMessage(objectMapper.writeValueAsString(history));
                } else if (job.equals("upload song")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String id = (jsonObject.getString("id"));
                    File file = new File("src/main/java/com/example/melodyhub/Server/download/"+id+".mp3");
                    if(file.exists()) {
                        //sendMessage("sending song");
                        MelodyHub.uploadSong(socket,"src/main/java/com/example/melodyhub/Server/download/"+id+".mp3");
                    }else
                        System.out.println();
                        //sendMessage("failed");
                }
                else if (job.equals("download song")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String id = (jsonObject.getString("id"));
                    MelodyHub.downloadSong(socket, id.toString());
                }
                else if (job.equals("upload image")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    File file = new File("src/main/java/com/example/melodyhub/Server/download/"+id+".png");
                    if(file.exists()) {
                        sendMessage("sending cover");
                        MelodyHub.uploadImage(socket,id.toString());
                    }else
                        sendMessage("failed");
                }
                else if (job.equals("download image")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = UUID.fromString(jsonObject.getString("id"));
                    MelodyHub.downloadImage(socket, id.toString());
                } else if (job.equals("listen")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String id = jsonObject.getString("id");
                    UserPerform.addHistory(account.getId(),"listen",id);
                    jsonObject.put("job",job);
                    MelodyHub.addLog(jsonObject);
                } else if (job.equals("download music cover")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String id = jsonObject.getString("id");
                    File file = new File("src/main/java/com/example/melodyhub/Server/download/"+id+".png");
                    if(!file.exists()) {
                        try {
                            MelodyHub.extractCover(id);
                            sendMessage("sending cover");
                            MelodyHub.uploadImage(socket, "src/main/java/com/example/melodyhub/Server/download/" + id + ".png");
                        } catch (Exception e) {
                            sendMessage("no cover");
                        }
                    }else
                    {
                        sendMessage("sending cover");
                        MelodyHub.uploadImage(socket, "src/main/java/com/example/melodyhub/Server/download/" + id + ".png");
                    }
                } else if (job.equals("logout")) {
                    account=null;
                    break;
                }
            }
            loXdySocket.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkTOTP(String email) throws IOException {
        sendMessage("TOTP");
        loXdyOutput.println("TOTP");
        loXdyOutput.println(email);
        loXdyOutput.println(getMessage());
        String check = loXdyInput.readLine();
        if(Objects.equals(check, "true")) {
            return true;
        }else
            return false;
    }
}