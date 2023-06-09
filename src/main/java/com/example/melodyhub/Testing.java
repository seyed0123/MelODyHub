package com.example.melodyhub;

import com.example.melodyhub.Server.MelodyHub.Session;
import com.example.melodyhub.Server.loXdy.Main;
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
import java.util.Base64;
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
        JSONArray jsonElements = new JSONArray();
        jsonElements.put("mamad");
        jsonObject.put("artists",jsonElements);
        sendMessage(jsonObject.toString());

    }
    @Test
    public void getSong() throws IOException {
        loginUser();
        sendMessage("get song");
        sendMessage("401e58e6-8b50-4b14-8170-aff3b337e0d5");
        ObjectMapper objectMapper = new ObjectMapper();
        Song song = objectMapper.readValue(getMessage(),Song.class);
        assertEquals("I think",song.getName());
        assertEquals(2020,song.getYear());
    }
}
