package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static com.example.melodyhub.LoginSignupPage.*;

public class Comment implements Initializable {
    public static Song song;
    @FXML
    private ListView<String> area;

    @FXML
    private TextField bar;

    public static void setSong(String song) {
        sendMessage("get song");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",song);
        sendMessage(jsonObject.toString());
        try {
            Comment.song = objectMapper.readValue(getMessage(),Song.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void submit(ActionEvent event) {
        sendMessage("comment");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("song",song.getId());
        jsonObject.put("comment",bar.getText());
        sendMessage(jsonObject.toString());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("you have been commented this song.");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendMessage("get comments");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("song",song.getId());
        sendMessage(jsonObject.toString());
        try {
            ArrayList<String> list = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
            area.getItems().addAll(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
