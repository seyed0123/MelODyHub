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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import java.util.*;

import static com.example.melodyhub.homepage_artist_podcaster_controller.*;

public class SearchPageController implements Initializable {

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

    // media playing attributes

    @FXML
    private Slider play_progress_bar;

    @FXML
    private Label song_name_label;

    @FXML
    private ImageView home_button;

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

    // media playing functions ------------------------------------------------------



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


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        if(songs == null) {
            songs = new ArrayList<File>();

            directory = new File("src/main/resources/com/example/melodyhub/musics");

            files = directory.listFiles();

            if (files != null) {

                for (File file : files) {

                    songs.add(file);
                }
            }

//            media = new Media(songs.get(songNumber).toURI().toString());
//            mediaPlayer = new MediaPlayer(media);

            media = homepage_artist_podcaster_controller.media;
            mediaPlayer = homepage_artist_podcaster_controller.mediaPlayer;

            song_name_label.setText(songs.get(songNumber).getName());
            song_name_label.setWrapText(true);
        }
        else{
            song_name_label.setText(songs.get(songNumber).getName());
            song_name_label.setWrapText(true);
            continueTimer();
        }


    }

    public void playMedia() {

        beginTimer();
//        changeSpeed(null);
//        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
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

//    public void changeSpeed(ActionEvent event) {
//
//        if(speedBox.getValue() == null) {
//
//            mediaPlayer.setRate(1);
//        }
//        else {
//
//            //mediaPlayer.setRate(Integer.parseInt(speedBox.getValue()) * 0.01);
//            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
//        }
//    }

    public void beginTimer() {

        timer = new Timer();

        task = new TimerTask() {

            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();

                play_progress_bar.setMin(0);  // Minimum value
                play_progress_bar.setMax(end);  // Maximum value, where `totalDuration` is the duration of the song in seconds


                play_progress_bar.setValue(current);

                if(current == end) {

                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }


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

    // pop up------------------------------------------------

    public void open_home() throws IOException {

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

//        setSocket();
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
