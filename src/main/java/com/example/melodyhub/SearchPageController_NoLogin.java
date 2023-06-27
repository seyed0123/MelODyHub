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

import static com.example.melodyhub.LoginSignupPage.*;

public class SearchPageController_NoLogin {

    @FXML
    private TextField search_field;

    //------------------------------------------------------

    @FXML
    public void searchClicked() throws IOException {
        sendMessage("search");
        sendMessage(search_field.getText());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<ArrayList<UUID>> list = objectMapper.readValue(json, new TypeReference<ArrayList<ArrayList<UUID>>>() {
        });
        System.out.println(list);
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

        String genre = id.equals("hiphop") ? "hip-hop" : id;

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

    private List<Song> getSongsById(List<UUID> uuidList) throws IOException {
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
    public void back()
    {
        ((Stage) search_field.getScene().getWindow()).close();
        try {
            new HomePage_NoLogin().start(new Stage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
