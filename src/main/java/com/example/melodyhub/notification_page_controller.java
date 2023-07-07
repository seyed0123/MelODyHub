package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.melodyhub.HomeController.*;
import static com.example.melodyhub.HomeController.current_song_name;
import static com.example.melodyhub.homepage_artist_podcaster_controller.*;
import static com.example.melodyhub.LoginSignupPage.*;

public class notification_page_controller implements Initializable {
    private static User user;
    @FXML
    private ImageView banner;

    @FXML
    private ImageView home_button;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane play_bar;

    @FXML
    private VBox recommended;

    @FXML
    private VBox side_bar;

    @FXML
    private BorderPane songsPane;

    @FXML
    private Label song_name_label;

    @FXML
    private Slider play_progress_bar;

    @FXML
    private ListView<Button> historyList;

    public static void setUser(User user) {
        notification_page_controller.user = user;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        {

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
            song_name_label.setText(current_song_name);
            song_name_label.setWrapText(true);

            continueTimer();
        }
        sendMessage("refresh notif");
        try {
            ArrayList<String> list = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
            user.setNotification(list);
            for(String str :list)
            {
                Button button = new Button();
                button.setText(str);
                button.setTextFill(Color.CORAL);
                button.setOnMouseClicked(event ->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("showing notification");
                    alert.setContentText(button.getText());
                    alert.showAndWait();
                    historyList.getItems().remove(button);
                    historyList.refresh();
                    user.removeNotif(button.getText());
                    sendMessage("save user notif");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("notif", objectMapper.writeValueAsString(user.getNotification()));
                        jsonObject.put("oldNotif", objectMapper.writeValueAsString(new ArrayList<String>()));
                        jsonObject.put("queue", objectMapper.writeValueAsString(user.getQueue()));
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    sendMessage(jsonObject.toString());
                });
                historyList.getItems().add(button);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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




    @FXML
    void open_home(MouseEvent event) throws IOException {
        FXMLLoader loader=null;
            loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) home_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);

    }

}
