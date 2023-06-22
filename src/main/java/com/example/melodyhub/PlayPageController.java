package com.example.melodyhub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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

public class PlayPageController {

    @FXML
    private Label durationLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private TextArea lyricsText;

    @FXML
    private VBox playlist_vb;

    @FXML
    private Label rateLabel;

    @FXML
    private Label singerLabel1;

    @FXML
    private Label singerLabel2;

    @FXML
    private ImageView songImage;

    @FXML
    private Label songNameLabel1;

    @FXML
    private Label songNameLabel2;

    @FXML
    private Label yearLabel;


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

    private List<Artist> getArtist(Song song) throws IOException {
        setSocket();
        sendMessage("get artist song");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", song.getId());
        sendMessage(jsonObject.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<Artist> artists = new ArrayList<>();
        return artists;
    }

    public void setPageContent(List<Song> playlist, Song playingSong) throws IOException {
        List<Artist> artists = getArtist(playingSong);
        // set sing information
        songNameLabel1.setText(playingSong.getName());
        songNameLabel2.setText(playingSong.getName());
        singerLabel1.setText(artists.get(0).getUsername());
        singerLabel2.setText(artists.get(0).getUsername());
        genreLabel.setText(playingSong.getGenre());
        durationLabel.setText(String.valueOf(playingSong.getDuration()));
        yearLabel.setText(String.valueOf(playingSong.getYear()));
        rateLabel.setText(String.valueOf(playingSong.getRate()));
        songImage.setImage(new Image(playingSong.getPath())); // todo test this

        // set playlist
        playlist_vb.getChildren().clear();
        for (Song song : playlist) {
            SongHBox songHBox = new SongHBox(song);
            playlist_vb.getChildren().add(songHBox);
        }

        // set lyrics
        lyricsText.setText(playingSong.getLyrics());
    }
}
