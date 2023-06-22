package com.example.melodyhub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class UserProfilePageController {

    @FXML
    private Label acctype_label;

    @FXML
    private Label age_label;

    @FXML
    private Label email_label;

    @FXML
    private Label followers_label;

    @FXML
    private Label followings_label;

    @FXML
    private Label gender_label;

    @FXML
    private Label phone_label;

    @FXML
    private ListView<PlayList> playlists_listview;

    @FXML
    private ImageView profile_img;

    @FXML
    private Label username_label;

    @FXML
    private ListView<User> followings_listview;

    @FXML
    private ListView<User> followers_listview;

    User user;

    // socket ---------------------------------------------
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

    public static void sendMessage(String message) {
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

    public static String getMessage() {
        try {
            String base64 = gson.fromJson(input.readLine(), String.class);
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

    public static void startCom() {
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

            String base64 = gson.fromJson(input.readLine(), String.class);
            byte[] encryptedMessage = Base64.getDecoder().decode(base64);
            byte[] decryptedMessage = decryptCipher.doFinal(encryptedMessage);

            cipherDecrypt = Cipher.getInstance("AES");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptedMessage, "AES"));
            cipherEncrypt = Cipher.getInstance("AES");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(decryptedMessage, "AES"));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
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

    public void setSocket() throws IOException {
        socket = new Socket(HOST, PORT);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        objOut = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());
        gson = new Gson();
        startCom();
    }
    // ------------------------------------------------------

    public void setProfileInfo() throws IOException {
        profile_img.setImage(new Image(user.getImage()));
        username_label.setText(user.getUsername());
        acctype_label.setText(user.isPremium() ? "Premium" : "Normal");
        age_label.setText(String.valueOf(user.getAge()));
        phone_label.setText(user.getPhoneNumber());
        email_label.setText(user.getEmail());
        gender_label.setText(user.getGender());

        followers_label.setText(String.valueOf(getFollowers().size()));
        followings_label.setText(String.valueOf(getFollowings().size()));
    }

    public void fillLists() throws IOException {
        playlists_listview.getItems().addAll(getPlaylists());
        followings_listview.getItems().addAll(getFollowings());
        followers_listview.getItems().addAll(getFollowers());
    }

    private List<User> getFollowings() throws IOException {
        setSocket();
        sendMessage("get followings");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<User> followings = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            objectMapper = new ObjectMapper();
            json = getMessage();
            User user = objectMapper.readValue(json, User.class);
            followings.add(user);
        }

        return followings;
    }

    private List<User> getFollowers() throws IOException {
        setSocket();
        sendMessage("get followers");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<User> followers = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            objectMapper = new ObjectMapper();
            json = getMessage();
            User user = objectMapper.readValue(json, User.class);
            followers.add(user);
        }

        return followers;
    }

    private List<PlayList> getPlaylists() throws IOException {
        setSocket();
        sendMessage("get playlists");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<PlayList> playLists = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get playlist");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            objectMapper = new ObjectMapper();
            json = getMessage();
            PlayList playList = objectMapper.readValue(json, PlayList.class);
            playLists.add(playList);
        }
        return playLists;
    }
}