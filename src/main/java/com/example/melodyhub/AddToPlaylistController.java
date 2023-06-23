package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.example.melodyhub.LoginSignupPage.*;

public class AddToPlaylistController implements Initializable {

    private static Song song;

    private static Account account;
    @FXML
    private Button addPlayList;

    @FXML
    private Label durationLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private ListView<HBox> playlists;

    @FXML
    private Label rateLabel;

    @FXML
    private Label singerLabel1;

    @FXML
    private ImageView songImage;

    @FXML
    private Label songNameLabel1;

    @FXML
    private Label yearLabel;

    @FXML
    private Button shareSong;
    public static void setAccount(Song song,Account account)
    {
        AddToPlaylistController.song=song;
        AddToPlaylistController.account=account;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendMessage("get artist song");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",song.getId());
        sendMessage(jsonObject.toString());
        try {
            ArrayList<UUID> artists = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<UUID>>() {});
            yearLabel.setText(song.getYear()+"");
            songNameLabel1.setText(song.getName());
            rateLabel.setText(song.getRate()+"");
            String id = song.getId();
            File file = new File("src/main/resources/com/example/melodyhub/images/covers/"+id+".png");
            try {
                if (!file.exists()) {
                    sendMessage("download music cover");
                    sendMessage(jsonObject.toString());
                    String response = getMessage();
                    if(response.equals("sending cover"))
                    {
                        Thread thread =new Thread(() -> downloadImage(socket,song.getId()));
                        thread.start();
                        thread.join();
                    }else
                    {
                        throw new Exception();
                    }
                }
                Image image = new Image(Account.class.getResource("images/covers/"+id+".png").toExternalForm());
                songImage.setImage(image);
            }catch (Exception e) {
                try{
                    Image image = new Image(Account.class.getResource("images/default.png").toExternalForm());
                    songImage.setImage(image);
                }catch (Exception ep)
                {
                    ep.printStackTrace();
                }
            }
            for(UUID uuid :artists)
            {
                sendMessage("get artist");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id",uuid);
                sendMessage(jsonObject1.toString());
                Artist artist = objectMapper.readValue(getMessage(),Artist.class);
                singerLabel1.setText(singerLabel1.getText()+"  "+artist.getUsername());
            }
            genreLabel.setText(song.getGenre());
            durationLabel.setText(song.getDuration()+"");
            sendMessage("get playlists");
            ArrayList<UUID> playlists = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<UUID>>() {});
            for(UUID uuid :playlists)
            {
                sendMessage("get playlist");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id",uuid);
                sendMessage(jsonObject1.toString());
                PlayList playlist = objectMapper.readValue(getMessage(),PlayList.class);
                // Load the image
                Image image = new Image(Account.class.getResource("images/default_playlist.png").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100.0);
                imageView.setFitWidth(100.0);
                imageView.setPickOnBounds(true);
                imageView.setPreserveRatio(true);
                imageView.setImage(image);
                // Create the VBox
                HBox Hbox = new HBox();
                Hbox.setAlignment(Pos.CENTER_LEFT);
                Hbox.setSpacing(10);
                Hbox.setPadding(new Insets(10));
                Hbox.getChildren().add(imageView);

                // Add the labels to the VBox
                Label playlistLabel = new Label(playlist.getName());
                Label durationLabel = new Label(playlist.getDuration()+"");
                Label personalLabel = new Label(playlist.isPersonal()+"");
                Hbox.getChildren().addAll(playlistLabel, durationLabel, personalLabel);
                this.playlists.getItems().add(Hbox);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void create(ActionEvent even) {
    // Create the text fields
        TextField textField = new TextField();

        // Create the checkbox
        CheckBox checkBox = new CheckBox();
        Stage stage = new Stage();
        // Create the VBox
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                new Label("Enter a name:"),
                textField,
                new Label("Is the playlist personal ?:"),
                checkBox);

        // Create the submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            String inputString = textField.getText();
            boolean inputBoolean = checkBox.isSelected();
            sendMessage("create playlist");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",inputString);
            jsonObject.put("personal",inputBoolean);
            jsonObject.put("artist","null");
            jsonObject.put("firstOwner",account.getUsername());
            sendMessage(jsonObject.toString());
            stage.close();
        });

        // Add the submit button to the VBox
        vbox.getChildren().add(submitButton);

        // Create the scene
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void shareSong(ActionEvent even) {
        TextField textField = new TextField();

        Stage stage = new Stage();
        // Create the VBox
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                new Label("Enter a name:"),
                textField);

        // Create the submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            String inputString = textField.getText();
            sendMessage("share song");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user",inputString);
            jsonObject.put("song",song.getId());
            sendMessage(jsonObject.toString());
            stage.close();
        });

        // Add the submit button to the VBox
        vbox.getChildren().add(submitButton);

        // Create the scene
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }
}
