package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

import static com.example.melodyhub.LoginSignupPage.*;
import static com.example.melodyhub.homepage_artist_podcaster_controller.*;
import static com.example.melodyhub.HomeController.*;

public class PlayPageController implements Initializable {

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

    @FXML
    private ListView song_list_view;

    @FXML
    private ImageView home_button;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
//
        for (File file : songs) {
            try {
                sendMessage("get song");
                JSONObject jsonObject = new JSONObject();
                String song_name = file.getName();
                int lastBackslashIndex = song_name.lastIndexOf("\\");
                int lastDotIndex = song_name.lastIndexOf(".");
                String fileName = song_name.substring(lastBackslashIndex + 1, lastDotIndex);
                jsonObject.put("id", fileName);
                sendMessage(jsonObject.toString());

                Song song = objectMapper.readValue(getMessage(), Song.class);

                Image image = new Image(Account.class.getResource("images/default.png").toExternalForm());
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

                // downloading cover

                try {
                    if (!file.exists()) {
                        sendMessage("download music cover");
                        sendMessage(jsonObject.toString());
                        String response = getMessage();
                        if (response.equals("sending cover")) {
                            Thread thread = new Thread(() -> downloadImage(socket, file.getName().substring(file.getName().length() - 4, file.getName().length() - 1)));
                            thread.start();
                            thread.join();
                        } else {
                            throw new Exception();
                        }
                    }
                    Image cover_image = new Image(Account.class.getResource("images/covers/" + fileName + ".png").toExternalForm());
                    imageView.setImage(image);
                } catch (Exception e) {
                    try {
                        Image cover_image = new Image(Account.class.getResource("images/default.png").toExternalForm());
                        imageView.setImage(image);
                    } catch (Exception ep) {
                        ep.printStackTrace();
                    }
                }

                // Add the labels to the VBox
                Label playlistLabel = new Label(song.getName());

//                    Label durationLabel = new Label(playlist.getDuration() + "");
//                    Label personalLabel = new Label(playlist.isPersonal() + "");
                Hbox.getChildren().addAll(playlistLabel);
                this.song_list_view.getItems().add(Hbox);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        songNameLabel1.setText(current_song_name);
        genreLabel.setText(current_song_genre);
        durationLabel.setText(current_song_duration);
        yearLabel.setText(current_song_year);
        rateLabel.setText(current_song_rate);
        lyricsText.setText(current_song_lyrics);


    }




    private List<Artist> getArtist(Song song) throws IOException {
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

    @FXML
    void open_home(MouseEvent event) throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) home_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);

    }
}


