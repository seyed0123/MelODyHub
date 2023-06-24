package com.example.melodyhub.Server.MelodyHub;

import com.example.melodyhub.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.awt.image.BufferedImage;

public class MelodyHub {
    private User user;
    public static Connection connection;
    public static Gson gson = new GsonBuilder().create();

    public MelodyHub(User user) {
        this.user = user;
    }

    public static synchronized ResultSet sendQuery(String query) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs;
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
    public static String hashPassword(String password)
    {
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }
    public static boolean checkPassword(String plain,String hashed)
    {
        return BCrypt.checkpw(plain,hashed);
    }
    public static User userLogin(String username, String password) {
        ResultSet res = MelodyHub.sendQuery("select id , pass from person where username ='"+username+"';");
        if(res==null)
        {
            return null;
        }
        try {
            if(!checkPassword(password,res.getString("pass")))
                return null;
            return findUser(UUID.fromString(res.getString("id")));
        } catch (SQLException e) {
            return null;
        }
    }
    public static Podcaster podcasterLogin(String username, String password) {
        ResultSet res = MelodyHub.sendQuery("select id , pass from podcaster where username ='"+username+"';");
        if(res==null)
        {
            return null;
        }
        try {
            if(!checkPassword(password,res.getString("pass")))
                return null;
            return findPodcaster(UUID.fromString(res.getString("id")));
        } catch (SQLException e) {
            return null;
        }
    }
    public static Artist artistLogin(String username, String password) {
        ResultSet res = MelodyHub.sendQuery("select id , pass from artist where username ='"+username+"';");
        if(res==null)
        {
            return null;
        }
        try {
            if(!checkPassword(password,res.getString("pass")))
                return null;
            return findArtist(UUID.fromString(res.getString("id")));
        } catch (SQLException e) {
            return null;
        }
    }
    public static  UUID findSongName(String name , String genre , int year)
    {
        ResultSet res = MelodyHub.sendQuery(String.format("select id from song where name = '%s' and genre ='%s' and year = %d ;",name,genre,year));
        if(res==null)
        {
            return null;
        }
        try {
            return UUID.fromString(res.getString("id"));
        } catch (SQLException e) {
            return null;
        }
    }
    public static UUID findUserUsername(String username)
    {
        ResultSet res = MelodyHub.sendQuery("select id from person where username = '"+username+"';");
        if(res==null)
        {
            return null;
        }
        try {
            return UUID.fromString(res.getString("id"));
        } catch (SQLException e) {
            return null;
        }
    }
    public static UUID findArtistUsername(String username)
    {
        ResultSet res = MelodyHub.sendQuery("select id from artist where username = '"+username+"';");
        if(res==null)
        {
            return null;
        }
        try {
            return UUID.fromString(res.getString("id"));
        } catch (SQLException e) {
            return null;
        }
    }
    public static UUID findPodcasterUsername(String username)
    {
        ResultSet res = MelodyHub.sendQuery("select id from podcaster where username = '"+username+"';");
        if(res==null)
        {
            return null;
        }
        try {
            return UUID.fromString(res.getString("id"));
        } catch (SQLException e) {
            return null;
        }
    }
    public static User findUser(UUID user)
    {
        ResultSet res = MelodyHub.sendQuery("select * from person where id ='"+user+"';");
        if(res==null)
        {
            return null;
        }
        try {
            return new User(res.getString("id"),res.getString("username"),res.getString("pass"),res.getString("email"),res.getString("phone"),res.getString("image")
                    ,gson.fromJson(res.getString("queue"),new TypeToken<ArrayList<UUID>>(){}.getType()),res.getString("image_Story"),res.getString("gender"),res.getDate("age") , null,res.getBoolean("premium"),gson.fromJson(res.getString("notifications"), new TypeToken<ArrayList<String>>(){}.getType()),gson.fromJson(res.getString("old_notification"), new TypeToken<ArrayList<String>>(){}.getType()));
        } catch (SQLException e) {
            return null;
        }
    }

    public static Artist findArtist(UUID artist) {
        ResultSet res = MelodyHub.sendQuery("select * from artist where id = '" + artist + "';");
        if(res==null)
            return null;
        try {
            return new Artist(res.getString("id"),res.getString("username"),res.getString("pass"),res.getString("email"),res.getString("phone"),res.getString("image"),res.getString("bio"),res.getBoolean("verify"),res.getInt("listeners"),res.getDouble("rate"),res.getString("genre"));
        } catch (SQLException e)
        {
            return null;
        }
    }

    public static Podcaster findPodcaster(UUID podcaster)
    {
        ResultSet res = MelodyHub.sendQuery("select * from podcaster where id = '" + podcaster + "';");
        if(res==null)
            return null;
        try {
            return new Podcaster(res.getString("id"),res.getString("username"),res.getString("pass"),res.getString("email"),res.getString("phone"),res.getString("image"),res.getBoolean("verify"),res.getString("bio"),res.getDouble("rate"));
        } catch (SQLException e)
        {
            return null;
        }
    }
    public static Song findSong(String song)
    {
        ResultSet res = MelodyHub.sendQuery("select * from song where id = '"+song+"'");
        if(res==null)
            return null;
        try {
            return new Song(res.getString("id"),res.getString("name"),res.getString("genre"),res.getDouble("duration"),res.getInt("year"),res.getDouble("rate"),res.getString("lyrics"), res.getString("path"));
        } catch (SQLException e) {
            return null;
        }
    }

    public static PlayList findPlaylist(UUID playlist)
    {
        ResultSet res = MelodyHub.sendQuery("select * from playlists where id ='"+playlist+"'");
        if(res==null)
            return null;
        try {
            return new PlayList(res.getString("id"), res.getString("name"), res.getBoolean("is_public"),res.getDouble("rate"),res.getDouble("duration"),(res.getString("artist")),res.getString("first_owner"));
        } catch (SQLException e) {
            return null;
        }
    }

    public static Podcast findPodcast(UUID podcast)
    {
        ResultSet res = MelodyHub.sendQuery("select * from song where id ='"+podcast+"'");
        if(res==null)
            return null;
        try {
            return new Podcast(res.getString("id"),res.getString("name"),res.getString("genre"),res.getDouble("duration"),res.getInt("year"),res.getDouble("rate"),res.getString("lyrics"),res.getString("path"),res.getString("description"));
        } catch (SQLException e) {
            return null;
        }
    }

    public static void createPlaylist(UUID firstOwner,PlayList playList)
    {
        MelodyHub.sendQuery(String.format("insert into playlists (id, duration, is_public, rate, artist, first_owner , name) VALUES ('%s',%.2f ,%b ,%.2f , '%s' ,'%s','%s')", playList.getId(),playList.getDuration(),playList.isPersonal(),playList.getRate(),playList.getArtist(),playList.getFirstOwner(),playList.getName()));
        AccountPerform.addPlaylist(firstOwner,playList.getId());
    }

    public static void removePlaylist(UUID playlist)
    {

    }

    public static void createSong(Song song,ArrayList<String> artists)
    {
        MelodyHub.sendQuery(String.format("insert into song (id, name, genre, duration, year, rate, lyrics, path) VALUES ( '%s' , '%s', '%s' ,%.2f ,  %d ,  %.3f ,'%s' , '%s');",song.getId(),song.getName(),song.getGenre(),song.getDuration(),song.getYear(),song.getRate(),song.getLyrics(),song.getPath()));
        for (String art :artists)
        {
            ArtistPerform.addSong(MelodyHub.findArtistUsername(art),song.getId());
        }
    }

    public static void removeSong(UUID song)
    {

    }

    public static void updateSong(UUID song , HashMap<String,String> command)
    {
        for(String column : command.keySet())
        {
            if(!Objects.equals(column, "id"))
                MelodyHub.sendQuery(String.format("update song set %s = %s where id = '%s';",column ,command.get(column),song));
        }
    }
    public static void createPodcast(Podcast podcast,UUID podcaster)
    {
        MelodyHub.sendQuery(String.format("insert into song (id, name, genre, duration, year, rate, lyrics, description, path) VALUES ('%s' , '%s', '%s' ,%.2f ,  %d ,  %.3f ,'%s', '%s', '%s');",podcast.getId(),podcast.getName(),podcast.getGenre(),podcast.getDuration(),podcast.getYear(),podcast.getRate(),podcast.getLyrics(),podcast.getDescription(),podcast.getPath()));
        PodcasterPerform.addPodcast(podcaster,podcast.getId());
    }
    public static void createUser(User user)
    {
        MelodyHub.sendQuery(String.format("insert into person (id, username, pass, email, phone, gender, age) VALUES ('%s','%s','%s','%s','%s','%s',CAST('"+user.getAge()+"' AS DATE));",user.getId(),user.getUsername(),hashPassword(user.getPassword()),user.getEmail(),user.getPhoneNumber(),user.getGender()));
    }

    public static void removeUser(UUID user)
    {

    }

    public static void updateUser(UUID user,HashMap<String,String> command)
    {
        for(String column : command.keySet())
        {
            if(!Objects.equals(column, "id") && !Objects.equals(column, "pass"))
                MelodyHub.sendQuery(String.format("update person set %s = '%s' where id = '%s';",column ,command.get(column),user));
        }
    }
    public static void updatePass(String table,UUID account,String password)
    {
        MelodyHub.sendQuery(String.format("update %s set pass = '%s' where id = '%s';",table,hashPassword(password),account));
    }
    public static void updatePremium(String table,String column,UUID account,boolean res)
    {
        MelodyHub.sendQuery(String.format("update %s set %s = %b where id = '%s';",table,column,res,account));
    }
    public static void createArtist(Artist artist)
    {

        MelodyHub.sendQuery(String.format("insert into artist (id, username, pass, email, phone, image, verify, bio, listeners, rate) VALUES ('%s','%s','%s','%s','%s','%s',%b,'%s',%d,%.2f);",artist.getId(),artist.getUsername(),hashPassword(artist.getPassword()),artist.getEmail(),artist.getPhoneNumber(),artist.getImage(),artist.isVerify(),artist.getBio(),artist.getListeners(),artist.getRate()));
    }

    public static void removeArtist(UUID artist)
    {

    }

    public static void updateArtist(UUID Artist,HashMap<String,String> command)
    {
        for(String column : command.keySet())
        {
            if(!Objects.equals(column, "id") && !Objects.equals(column, "pass"))
                MelodyHub.sendQuery(String.format("update artist set %s = %s where id = '%s';",column ,command.get(column),Artist));
        }
    }

    public static void createPodcaster(Podcaster podcaster)
    {
        MelodyHub.sendQuery(String.format("insert into podcaster (id, username, pass, email, phone, image, verify, bio, rate) values ('%s', '%s','%s','%s','%s','%s',%b ,'%s',%.2f);",podcaster.getId(),podcaster.getUsername(),hashPassword(podcaster.getPassword()),podcaster.getEmail(),podcaster.getPhoneNumber(),podcaster.getImage(),podcaster.isVerify(),podcaster.getBio(),podcaster.getRate()));
    }

    public static void removePodcaster(UUID podcaster)
    {

    }


    public static void updatePodcaster(UUID podcaster,HashMap<String,String> command)
    {
        for(String column : command.keySet())
        {
            if(!Objects.equals(column, "id") && !Objects.equals(column, "pass"))
                MelodyHub.sendQuery(String.format("update podcaster set %s = %s where id = '%s';",column ,command.get(column),podcaster));
        }
    }

    public static void addLog(JSONObject jsonObject)
    {
        MelodyHub.sendQuery("insert into log (command) values ('"+jsonObject.toString()+"');");
    }
    public static void uploadImage(Socket socket,String path)
    {
        try
        {
            // Load the image from a file
            File file = new File(path);
            BufferedImage image = ImageIO.read(file);

            // Convert the image to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // Send the byte array over the socket
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(imageBytes.length);
            dos.write(imageBytes);
            dos.flush();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void downloadImage(Socket socket ,String path)
    {
        try {

            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            int length = dis.readInt();
            byte[] receivedBytes = new byte[length];
            dis.readFully(receivedBytes);

            // Convert the byte array back to an image
            BufferedImage receivedImage = ImageIO.read(new ByteArrayInputStream(receivedBytes));
            // Save the image to a file
            File file = new File("src/main/java/com/example/melodyhub/Server/download/"+path+".png");
            FileOutputStream fout = new FileOutputStream(file);
            ImageIO.write(receivedImage, "png", fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadSong(Socket socket,String path)
    {
        try{
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            int length = dis.readInt();
            byte[] receivedBytes = new byte[length];
            dis.readFully(receivedBytes);

            File file = new File("src/main/java/com/example/melodyhub/Server/download/"+path+".mp3");
            OutputStream out = new FileOutputStream(file);
            out.write(receivedBytes);
            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void uploadSong(Socket socket,String path)
    {
        try {
            OutputStream outputStream = socket.getOutputStream();
            int CHUNK_SIZE = 4096;
            File file = new File(path);
            long fileSize = file.length();
            int numChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);

            ArrayList<Thread> threads = new ArrayList<>();
            for (int i = 0; i < numChunks; i++) {
                int finalI = i;
                int offset = finalI * CHUNK_SIZE;
                int chunkSize = (int) Math.min(CHUNK_SIZE, fileSize - offset);
                byte[] buffer = new byte[chunkSize];
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                randomAccessFile.seek(offset);
                randomAccessFile.read(buffer);
                randomAccessFile.close();

                outputStream.write(buffer);
            }
            System.out.println("File sent successfully.");
        } catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
        }
    }
    public static ArrayList<String> search(String searched) {
        ArrayList<String> song = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery(String.format("SELECT id\n" +
                "FROM Song\n" +
                "WHERE name LIKE '%s'\n" +
                "   OR lyrics LIKE '%s'\n" +
                "    OR genre LIKE '%s'\n" +
                "    OR description LIKE '%s'", searched,searched,searched,searched,searched));
        if(res==null)
            return song;
        while (true) {
            try {
                if (!res.next()) break;
                song.add((res.getString("id")));
            } catch (SQLException e) {
                break;
            }
        }
        return song;
    }
    public static ArrayList<UUID> searchArtist(String searched)
        {
            ArrayList<UUID> artists = new ArrayList<>();
            ResultSet res = MelodyHub.sendQuery(String.format("SELECT id\n" +
                    "FROM artist\n" +
                    "WHERE username like '%s'\n" +
                    "OR genre like '%s';\n", searched,searched));
            if(res==null)
                return artists;
            while (true) {
                try {
                    artists.add(UUID.fromString(res.getString("id")));
                    if (!res.next()) break;
                } catch (SQLException e) {
                    break;
                }
            }
            return artists;
        }
    public static ArrayList<UUID> searchUser(String searched)
    {
        ArrayList<UUID> artists = new ArrayList<>();
        ResultSet res = MelodyHub.sendQuery(String.format("SELECT id\n" +
                "FROM person\n" +
                "WHERE username like '%s'\n" +
                ";\n", searched));
        if(res==null)
            return artists;
        while (true) {
            try {
                artists.add(UUID.fromString(res.getString("id")));
                if (!res.next()) break;
            } catch (SQLException e) {
                break;
            }
        }
        return artists;
    }
    public static ArrayList<UUID> getGenreArtist(String genre)
    {
        ArrayList<UUID> ret = new ArrayList<>();
        ResultSet res=MelodyHub.sendQuery(String.format("select id from artist where genre='%s';",genre));
        while (true) {
            try {
                ret.add(UUID.fromString(res.getString("id")));
                if (!res.next()) break;
            } catch (SQLException e) {
                break;
            }
        }
        return ret;
    }
    public static void extractCover(String id) throws Exception {
        File mp3File = new File("src/main/resources/com/example/melodyhub/musics/"+id+".mp3");
        Mp3File mp3 = new Mp3File(mp3File);

        if (mp3.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3.getId3v2Tag();
            byte[] imageData = id3v2Tag.getAlbumImage();

            if (imageData != null) {
                File coverFile = new File("src/main/java/com/example/melodyhub/Server/download/"+id+".png");
                FileUtils.writeByteArrayToFile(coverFile, imageData);
                System.out.println("Cover image saved to " + coverFile.getAbsolutePath());
            } else {
                System.out.println("No cover image found in the MP3 file.");
                throw new Exception();
            }
        } else {
            System.out.println("No ID3v2 tag found in the MP3 file.");
            throw new Exception();
        }
    }
}
