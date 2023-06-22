package com.example.melodyhub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

public class SearchPageController {

    @FXML
    private TextField search_field;

    // socket----------------------------------------------
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

    //------------------------------------------------------

    @FXML
    public void searchClicked() throws IOException {
        setSocket();
        sendMessage("search");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", search_field.getText());
        sendMessage(jsonObject.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<ArrayList<UUID>> list = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<Song> result1 = getSongsById(list.get(0));
        List<Song> result2 = getSongsById(list.get(1));
        List<Song> allResult = new ArrayList<>(result1);
        allResult.addAll(result2);
        System.out.println(allResult);

        ((Stage) search_field.getScene().getWindow()).close();
        new SongsListPage(allResult).start(new Stage());
    }

    private List<Song> getSongsById(List<UUID> uuidList) throws IOException {
        setSocket();
        List<Song> songList = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get song");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = getMessage();
            Song song = objectMapper.readValue(json, Song.class);
            songList.add(song);
        }
        return songList;
    }

    @FXML
    public void cardMouseEntered(MouseEvent e) {
        Pane p = (Pane) e.getSource();
        p.setPrefWidth(170);
        p.setPrefHeight(170);
    }

    @FXML
    public void cardMouseExited(MouseEvent e) {
        Pane p = (Pane) e.getSource();
        p.setPrefWidth(150);
        p.setPrefHeight(150);
    }

    @FXML
    public void cardClicked(Event event) throws IOException {
        Pane card = (Pane) event.getSource();
        String id = card.getId();

        String genre = switch (id) {
            case "hiphop" -> "hip-hop";
            case "electronic" -> "dance/electronic";
            case "kpop" -> "k-pop";
            case "rb" -> "R&B";
            case "dance" -> "dance music";
            case "newAge" -> "new-age music";
            case "country" -> "country music";
            default -> id;
        };

        setSocket();
        sendMessage("find song genre");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", genre);
        sendMessage(jsonObject.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });
        List<Song> songList = getSongsById(uuidList);

        ((Stage) search_field.getScene().getWindow()).close();
        new SongsListPage(songList).start(new Stage());
    }
}
