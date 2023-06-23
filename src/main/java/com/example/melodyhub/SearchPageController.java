package com.example.melodyhub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
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

public class SearchPageController implements Initializable {

    @FXML
    private TextField search_field;

    // socket----------------------------------------------
    // media player attributes ---------------------------------

    @FXML
    private Label artist_name_label;

    @FXML
    private ImageView banner;

    @FXML
    private ImageView comment;

    @FXML
    private ImageView like;

    @FXML
    private ImageView lyrics;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ImageView next_track;

    @FXML
    private ImageView pause_button;

    @FXML
    private Pane play_bar;

    @FXML
    private ImageView play_button;

    @FXML
    private Slider play_progress_bar;

    @FXML
    private ImageView previous_track;

    @FXML
    private ImageView queue;

    @FXML
    private VBox recommended;

    @FXML
    private VBox side_bar;

    @FXML
    private Label song_name_label;

    @FXML
    private BorderPane songsPane;


    // pop ups

    @FXML
    private ImageView home_button;

    // media playing functions ----------------------------------------

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        if (songs == null) {
            songs = new ArrayList<File>();

            directory = new File("src/main/resources/com/example/melodyhub/musics");

            files = directory.listFiles();

            if (files != null) {

                for (File file : files) {

                    songs.add(file);
                }
            }

            //        media = new Media(songs.get(songNumber).toURI().toString());
            //        mediaPlayer = new MediaPlayer(media);

            media = homepage_artist_podcaster_controller.media;
            mediaPlayer = homepage_artist_podcaster_controller.mediaPlayer;

            song_name_label.setText(songs.get(songNumber).getName());
            song_name_label.setWrapText(true);

        } else {
            song_name_label.setText(songs.get(songNumber).getName());
            song_name_label.setWrapText(true);

//            // updating the current playing time
            play_progress_bar.setValue(current_play_time);
            continueTimer();


        }

    }

    // media playing functions ------------------------------------------------------

    public void playMedia() {

        beginTimer();
        mediaPlayer.play();
    }

    public void pauseMedia() {

        cancelTimer();
        mediaPlayer.pause();
    }

    public void resetMedia() {

        play_progress_bar.setValue(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void previousMedia() {

        if(songNumber > 0) {

            songNumber--;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            song_name_label.setText(songs.get(songNumber).getName());

            playMedia();
        }
        else {

            songNumber = songs.size() - 1;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            song_name_label.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }

    public void nextMedia() {

        if(songNumber < songs.size() - 1) {

            songNumber++;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            song_name_label.setText(songs.get(songNumber).getName());

            playMedia();
        }
        else {

            songNumber = 0;

            mediaPlayer.stop();

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            song_name_label.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }

    public void beginTimer() {

        timer = new Timer();

        task = new TimerTask() {

            public void run() {

                running = true;
                current_play_time = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();

                play_progress_bar.setMin(0);  // Minimum value
                play_progress_bar.setMax(end);  // Maximum value, where `totalDuration` is the duration of the song in seconds


                play_progress_bar.setValue(current_play_time);

                if(current_play_time == end) {

                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // for continuing the timer and setting the progress bar
    public void continueTimer() {

        timer = new Timer();

        task = new TimerTask() {

            public void run() {

                running = true;
                current_play_time = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();

                play_progress_bar.setMin(0);  // Minimum value
                play_progress_bar.setMax(end);  // Maximum value, where `totalDuration` is the duration of the song in seconds


                play_progress_bar.setValue(current_play_time);

                if(current_play_time == end) {

                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer() {

        running = false;
        timer.cancel();
    }

    public void set_play_time(){

        double playbackPosition = play_progress_bar.getValue();
        mediaPlayer.seek(Duration.seconds(playbackPosition));
        playMedia();

    }


//
//    public void playMedia() {
//        homepage_artist_podcaster_controller.playMedia();
//    }
//
//    public void pauseMedia() {
//        homepage_artist_podcaster_controller.pauseMedia();
//    }
//
//    public void resetMedia() {
//        homepage_artist_podcaster_controller.resetMedia();
//    }
//
//    public void previousMedia() {
//        homepage_artist_podcaster_controller.previousMedia();
//    }
//
//    public void nextMedia() {
//        homepage_artist_podcaster_controller.nextMedia();
//    }
//
//    public void beginTimer() {
//        homepage_artist_podcaster_controller.beginTimer();
//    }
//
//    public void cancelTimer() {
//        homepage_artist_podcaster_controller.cancelTimer();
//    }
//
//    public void set_play_time() {
//        homepage_artist_podcaster_controller.set_play_time();
//    }


    // pop up --------------------------------

    @FXML
    void open_home(MouseEvent event) throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage_artist&podcater.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) home_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);

    }

    // ------------------------------------------------------------------------------------

    @FXML
    public void searchClicked() throws IOException {
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
