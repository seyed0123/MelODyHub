package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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


public class HomePage_NoLoginController implements Initializable {
    @FXML
    private ImageView banner;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane play_bar;

    @FXML
    private HBox popular;

    @FXML
    private HBox recomm;

    @FXML
    private VBox recommended;

    @FXML
    private VBox side_bar;

    @FXML
    private BorderPane songsPane;


    public static Media media;
    public static MediaPlayer mediaPlayer;

    public static File directory;
    public static File[] files;

    public static  ArrayList<File> songs;

    public static  int songNumber;
    public static Timer timer;
    public static  TimerTask task;

    public static  boolean running;

    public static double current_play_time;

    @FXML
    public Label song_name_label;

    @FXML
    private Slider play_progress_bar;

    public static String current_song_name ;
    public static String current_song_artist;
    public static String current_song_genre;
    public static String current_song_duration;
    public static String current_song_rate;
    public static String current_song_year;

    public static String current_song_id;

    public static String current_song_lyrics;

    @FXML
    void login(MouseEvent event) throws IOException {
        Stage l = (Stage) recommended.getScene().getWindow();
        l.close();
        Stage stage = new Stage();
        pauseMedia();
        FXMLLoader fxmlLoader = new FXMLLoader(Account.class.getResource("LoginSignupPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login / Signup");
        stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
        stage.setTitle("MelOXDy hub!!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void serach(MouseEvent event) throws IOException {
        Stage stage = (Stage) popular.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(SearchPage_NoLogin.class.getResource("SearchPage_nologin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
        stage.setTitle("MelOXDy hub!!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendMessage("get popular");
        ArrayList<String> popular = null;
        try {
            popular = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for(String id :popular)
        {
            sendMessage("get song");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            sendMessage(jsonObject.toString());
            Song song = null;
            try {
                song = objectMapper.readValue(getMessage(), Song.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setSpacing(2.0);
            vbox.setMaxWidth(Double.NEGATIVE_INFINITY);
            vbox.setMinWidth(Double.NEGATIVE_INFINITY);
            vbox.setPrefHeight(150.0);
            vbox.setPrefWidth(100.0);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(100.0);
            imageView.setFitWidth(100.0);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);
            File file = new File("src/main/resources/com/example/melodyhub/images/profile/"+id+".png");
            try {
                if (!file.exists()) {
                    sendMessage("download music cover");
                    sendMessage(jsonObject.toString());
                    String response = getMessage();
                    if(response.equals("sending cover"))
                    {
                        Thread thread =new Thread(() -> downloadImage(socket,"src/main/resources/com/example/melodyhub/images/profile/"+id+".png"));
                        thread.start();
                        thread.join();
                    }else
                    {
                        throw new Exception();
                    }
                }
                Image image = new Image(Account.class.getResource("images/profile/"+id+".png").toExternalForm());
                imageView.setImage(image);
            }catch (Exception e) {
                try{
                    Image image = new Image(Account.class.getResource("images/default.png").toExternalForm());
                    imageView.setImage(image);
                }catch (Exception ep)
                {
                    ep.printStackTrace();
                }
            }
            Label songNameLabel = new Label(song.getName());
            songNameLabel.setPrefHeight(16.0);
            songNameLabel.setPrefWidth(139.0);
            songNameLabel.setTextFill(Color.WHITE);
            songNameLabel.setFont(new Font("Arial Nova", 16.0));

            Label singerNameLabel = new Label(song.getGenre());
            singerNameLabel.setPrefHeight(16.0);
            singerNameLabel.setPrefWidth(139.0);
            singerNameLabel.setTextFill(Color.WHITE);
            singerNameLabel.setFont(new Font("Arial Nova Light", 11.0));

            vbox.getChildren().addAll(imageView, songNameLabel, singerNameLabel);
            this.popular.getChildren().addAll(vbox);
        }
        songs = new ArrayList<File>();

        directory = new File("src/main/resources/com/example/melodyhub/musics");

        files = directory.listFiles();

        if (files != null) {

            for (File file : files) {

                songs.add(file);
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            // setting the current palying song variables

            File song_file = songs.get(songNumber);
            sendMessage("get song");
            JSONObject jsonObject = new JSONObject();
            String song_name = song_file.getName();
            int lastBackslashIndex = song_name.lastIndexOf("\\");
            int lastDotIndex = song_name.lastIndexOf(".");
            String fileName = song_name.substring(lastBackslashIndex + 1, lastDotIndex);
            jsonObject.put("id", fileName);
            sendMessage(jsonObject.toString());

            Song song = null;
            try {
                song = objectMapper.readValue(getMessage(), Song.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            current_song_id = fileName;
            current_song_name = song.getName();
            current_song_genre = song.getGenre();
            current_song_duration = String.valueOf(song.getDuration());
            current_song_year = String.valueOf(song.getYear());
            current_song_rate = String.valueOf(song.getRate());
            current_song_lyrics = song.getLyrics();
            song_name_label.setWrapText(true);
            song_name_label.setText(current_song_name);
        }
    }
    public void playMedia() {

        sendMessage("listen");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",current_song_id);
        sendMessage(jsonObject.toString());
        beginTimer();
        mediaPlayer.play();
    }

    public  void pauseMedia() {

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

//            current_song_name = songs.get(songNumber).getName();

            try {
                File song_file = songs.get(songNumber);
                sendMessage("get song");
                JSONObject jsonObject = new JSONObject();
                String song_name = song_file.getName();
                int lastBackslashIndex = song_name.lastIndexOf("\\");
                int lastDotIndex = song_name.lastIndexOf(".");
                String fileName = song_name.substring(lastBackslashIndex + 1, lastDotIndex);
                jsonObject.put("id", fileName);
                sendMessage(jsonObject.toString());

                Song song = objectMapper.readValue(getMessage(), Song.class);

                current_song_name = song.getName();
                current_song_genre = song.getGenre();
                current_song_duration = String.valueOf(song.getDuration());
                current_song_year = String.valueOf(song.getYear());
                current_song_rate = String.valueOf(song.getRate());
                song_name_label.setWrapText(true);

                song_name_label.setText(current_song_name);


                playMedia();

            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

        else {

            songNumber = songs.size() - 1;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            try {
                File song_file = songs.get(songNumber);
                sendMessage("get song");
                JSONObject jsonObject = new JSONObject();
                String song_name = song_file.getName();
                int lastBackslashIndex = song_name.lastIndexOf("\\");
                int lastDotIndex = song_name.lastIndexOf(".");
                String fileName = song_name.substring(lastBackslashIndex + 1, lastDotIndex);
                jsonObject.put("id", fileName);
                sendMessage(jsonObject.toString());

                Song song = objectMapper.readValue(getMessage(), Song.class);

                current_song_name = song.getName();
                current_song_genre = song.getGenre();
                current_song_duration = String.valueOf(song.getDuration());
                current_song_year = String.valueOf(song.getYear());
                current_song_rate = String.valueOf(song.getRate());
                song_name_label.setWrapText(true);

                song_name_label.setText(current_song_name);


                playMedia();

            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public  void nextMedia() {

        if(songNumber < songs.size() - 1) {

            songNumber++;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            try {
                File song_file = songs.get(songNumber);
                sendMessage("get song");
                JSONObject jsonObject = new JSONObject();
                String song_name = song_file.getName();
                int lastBackslashIndex = song_name.lastIndexOf("\\");
                int lastDotIndex = song_name.lastIndexOf(".");
                String fileName = song_name.substring(lastBackslashIndex + 1, lastDotIndex);
                jsonObject.put("id", fileName);
                sendMessage(jsonObject.toString());

                Song song = objectMapper.readValue(getMessage(), Song.class);

                current_song_name = song.getName();
                current_song_genre = song.getGenre();
                current_song_duration = String.valueOf(song.getDuration());
                current_song_year = String.valueOf(song.getYear());
                current_song_rate = String.valueOf(song.getRate());
                song_name_label.setWrapText(true);

                song_name_label.setText(current_song_name);


                playMedia();

            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        else {

            songNumber = 0;

            mediaPlayer.stop();

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            try {
                File song_file = songs.get(songNumber);
                sendMessage("get song");
                JSONObject jsonObject = new JSONObject();
                String song_name = song_file.getName();
                int lastBackslashIndex = song_name.lastIndexOf("\\");
                int lastDotIndex = song_name.lastIndexOf(".");
                String fileName = song_name.substring(lastBackslashIndex + 1, lastDotIndex);
                jsonObject.put("id", fileName);
                sendMessage(jsonObject.toString());

                Song song = objectMapper.readValue(getMessage(), Song.class);

                current_song_name = song.getName();
                current_song_genre = song.getGenre();
                current_song_duration = String.valueOf(song.getDuration());
                current_song_year = String.valueOf(song.getYear());
                current_song_rate = String.valueOf(song.getRate());
                song_name_label.setWrapText(true);

                song_name_label.setText(current_song_name);


                playMedia();

            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public   void beginTimer() {

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

    // for setting the progress bar after comming from another page
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


    public  void cancelTimer() {

        running = false;
        timer.cancel();
    }

    public   void set_play_time(){

        double playbackPosition = play_progress_bar.getValue();
        mediaPlayer.seek(Duration.seconds(playbackPosition));
        playMedia();

    }
}