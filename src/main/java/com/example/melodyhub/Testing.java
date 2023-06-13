package com.example.melodyhub;

import com.example.melodyhub.Server.MelodyHub.AccountPerform;
import com.example.melodyhub.Server.MelodyHub.Session;
import com.example.melodyhub.Server.loXdy.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.testng.AssertJUnit.*;

public class Testing {
    private static Socket socket;
    public static PrintWriter output;
    public static BufferedReader input;
    private static ObjectOutputStream objOut;
    private static ObjectInputStream objIn;
    private static final String HOST = "localhost";
    private static final int PORT = 8085;
    public static Cipher cipherEncrypt;
    public static Cipher cipherDecrypt;
    public static Gson gson;
    public static void sendMessage(String message)
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
    public static String getMessage()
    {
        try {
            String base64 = gson.fromJson(input.readLine(),String.class);
            byte[] decodedData = Base64.getDecoder().decode(base64);
            byte[] decrypted = cipherDecrypt.doFinal(decodedData);
            return new String(decrypted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static void startCom()
    {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            //ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
            objOut.writeObject(publicKey);
            objOut.flush();
            //objOut.close();

            String base64 = gson.fromJson(input.readLine(),String.class);
            byte[] encryptedMessage = Base64.getDecoder().decode(base64);
            byte[] decryptedMessage = decryptCipher.doFinal(encryptedMessage);

            cipherDecrypt = Cipher.getInstance("AES");
            cipherDecrypt.init(Cipher.DECRYPT_MODE,  new SecretKeySpec(decryptedMessage, "AES"));
            cipherEncrypt= Cipher.getInstance("AES");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(decryptedMessage, "AES"));
        }catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public void setSocket() throws IOException
    {
        socket = new Socket(HOST, PORT);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        objOut = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());
        gson = new Gson();
        startCom();
    }
    @Test
    public void createUser() throws IOException {
        setSocket();
        sendMessage("create user");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","seyed");
        jsonObject.put("password","1234");
        jsonObject.put("phone","12334656");
        jsonObject.put("email","seyed@gmail.com");
        jsonObject.put("gender","male");
        jsonObject.put("date","2000-09-12");
        sendMessage(jsonObject.toString());
        assertEquals("done",getMessage());
        sendMessage("create user");
        sendMessage(jsonObject.toString());
        assertEquals("failed",getMessage());
    }
    @Test
    public void testCreateMultipleUsers() throws IOException {
        setSocket();

        List<JSONObject> users = new ArrayList<>();
        users.add(createUserJson("johndoe", "password123", "1234567890", "johndoe@example.com", "male", "1990-01-01"));
        users.add(createUserJson("janedoe", "password456", "0987654321", "janedoe@example.com", "female", "1995-05-05"));
        users.add(createUserJson("bobsmith", "password789", "5551234567", "bobsmith@example.com", "male", "1985-12-31"));
        users.add(createUserJson("sallyjones", "passwordabc", "2225558888", "sallyjones@example.com", "female", "1980-07-15"));
        users.add(createUserJson("mikewilson", "passworddef", "5559876543", "mikewilson@example.com", "male", "1975-03-22"));

        for (JSONObject user : users) {
            sendMessage("create user");
            sendMessage(user.toString());
            assertEquals("done", getMessage());
        }

        for (JSONObject user : users) {
            sendMessage("create user");
            sendMessage(user.toString());
            assertEquals("failed", getMessage());
        }
    }

    private JSONObject createUserJson(String username, String password, String phone, String email, String gender, String date) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("phone", phone);
        jsonObject.put("email", email);
        jsonObject.put("gender", gender);
        jsonObject.put("date", date);
        return jsonObject;
    }
    @Test
    public void createArtist() throws IOException {
        setSocket();
        sendMessage("create artist");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","mamad");
        jsonObject.put("password","1234");
        jsonObject.put("phone","12334656");
        jsonObject.put("email","temp@gmail.com");
        sendMessage(jsonObject.toString());
        assertEquals("done",getMessage());
        sendMessage("create artist");
        sendMessage(jsonObject.toString());
        assertEquals("failed",getMessage());
    }
    @Test
    public void createMultipleArtists() throws IOException {
        // Set up the socket and send the "create artist" command
        setSocket();


        // Create a list of 5 artists
        List<JSONObject> artists = new ArrayList<>();
        artists.add(createArtistJson("adolf", "1234", "12334656", "adolf@example.com"));
        artists.add(createArtistJson("sara", "5678", "98765432", "sara@example.com"));
        artists.add(createArtistJson("ali", "9012", "54321678", "ali@example.com"));
        artists.add(createArtistJson("lisa", "3456", "87654321", "lisa@example.com"));
        artists.add(createArtistJson("john", "7890", "12345678", "john@example.com"));

        // Send each artist as a message and expect the response "done"
        for (JSONObject artist : artists) {
            sendMessage("create artist");
            sendMessage(artist.toString());
            assertEquals("done", getMessage());
        }

        // Try to create each artist again and expect the response "failed"
        for (JSONObject artist : artists) {
            sendMessage("create artist");
            sendMessage(artist.toString());
            assertEquals("failed", getMessage());
        }
    }

    private JSONObject createArtistJson(String username, String password, String phone, String email) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("phone", phone);
        jsonObject.put("email", email);
        return jsonObject;
    }
    @Test
    public void createPodcaster() throws IOException {
        setSocket();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username","mamad");
        jsonObject.put("password","1234");
        jsonObject.put("phone","12334656");
        jsonObject.put("email","temp@gmail.com");
        sendMessage("create podcaster");
        sendMessage(jsonObject.toString());
        assertEquals("done",getMessage());
        sendMessage("create podcaster");
        sendMessage(jsonObject.toString());
        assertEquals("failed",getMessage());
    }
    @Test
    public void loginUser() throws IOException {
        setSocket();
        sendMessage("login user");
        sendMessage("seyed");
        sendMessage("1234");
        assertEquals("TOTP",getMessage());
        sendMessage("149802");
        assertEquals("login OK",getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        User user = objectMapper.readValue(json, User.class);
        assertEquals("seyed",user.getUsername());
    }
    @Test
    public void forgotPass() throws IOException {
        setSocket();
        sendMessage("forget pass");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("work","TOTP");
        jsonObject.put("username","seyed");
        jsonObject.put("type","person");
        sendMessage(jsonObject.toString());
        sendMessage("149802");
        assertEquals("TOTP",getMessage());
        assertEquals("you are you",getMessage());
        sendMessage("1234");
        assertEquals("password updated",getMessage());
    }
    @Test
    public void ForgotPassQues() throws IOException {
        setSocket();
        sendMessage("forget pass");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("work","answer");
        jsonObject.put("username","seyed");
        jsonObject.put("type","user");
        jsonObject.put("answer","just melody");
        jsonObject.put("number",1);
        sendMessage(jsonObject.toString());
        assertEquals("you are you",getMessage());
        sendMessage("1234");
        assertEquals("password updated",getMessage());
    }
    @Test
    public void loginArtist() throws IOException {
        setSocket();
        sendMessage("login artist");
        sendMessage("mamad");
        sendMessage("1234");
        assertEquals("TOTP",getMessage());
        sendMessage("149802");
        assertEquals("login OK",getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        Artist artist = objectMapper.readValue(json, Artist.class);
        assertEquals("mamad",artist.getUsername());
    }

    @Test
    public void crateSong() throws IOException {
        loginArtist();
        sendMessage("create song");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","I think");
        jsonObject.put("genre" , "Pop");
        jsonObject.put("duration",2.53);
        jsonObject.put("year",2020);
        jsonObject.put("lyrics","none");
        jsonObject.put("rate",0.0);
        JSONArray artists = new JSONArray();
        artists.put("mamad");
        jsonObject.put("artists",artists);
        sendMessage(jsonObject.toString());
    }
    @Test
    public void createSongsWithMultipleSingers() throws IOException {
        setSocket();
        loginArtist();

        List<JSONObject> songs = new ArrayList<>();

        List<String> songNames = Arrays.asList("Gone with the Wind", "Dancing in the Dark", "Blue Moon", "Autumn Leaves", "Summertime", "Fly Me to the Moon", "The Way You Look Tonight");

        for (int i = 1; i <= 5; i++) {
            String songName = songNames.get(new Random().nextInt(songNames.size()));
            List<String> artists = Arrays.asList("mamad", "sara", "ali", "lisa", "john");
            Collections.shuffle(artists);
            List<String> singers = artists.subList(0, new Random().nextInt(artists.size()) + 1);
            JSONObject song = createSongJson(songName, "Jazz", 3.5, 2022, "", 0.0, singers);
            songs.add(song);
        }

        for (JSONObject song : songs) {
            sendMessage("create song");
            sendMessage(song.toString());
        }
    }

    private JSONObject createSongJson(String name, String genre, double duration, int year, String lyrics, double rate, List<String> singers) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("genre", genre);
        jsonObject.put("duration", duration);
        jsonObject.put("year", year);
        jsonObject.put("lyrics", lyrics);
        jsonObject.put("rate", rate);
        JSONArray artistsArray = new JSONArray(singers);
        jsonObject.put("artists", artistsArray);
        return jsonObject;
    }
    @Test
    public void getSong() throws IOException {
        loginUser();
        JSONObject jsonObject = new JSONObject(getMessage());
        jsonObject.put("id","401e58e6-8b50-4b14-8170-aff3b337e0d5");
        sendMessage(jsonObject.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        Song song = objectMapper.readValue(getMessage(),Song.class);
        assertEquals("I think",song.getName());
        assertEquals(2020,song.getYear());
    }
    @Test
    public void uploadImage() throws IOException {
        setSocket();
        loginUser();
        sendMessage("download image");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","6f51a2cb-feff-4ec7-a35b-928c1193b561");
        sendMessage(jsonObject.toString());
        HelloApplication.uploadImage(socket,"src/main/resources/com/example/melodyhub/image.png");
    }
}
