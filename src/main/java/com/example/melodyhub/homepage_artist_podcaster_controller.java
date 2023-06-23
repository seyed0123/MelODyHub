package com.example.melodyhub;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;


public class homepage_artist_podcaster_controller implements Initializable {


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
    private  Slider play_progress_bar;

    @FXML
    private ImageView previous_track;

    @FXML
    private ImageView queue;

    @FXML
    private VBox recommended;

    @FXML
    private VBox side_bar;

    @FXML
    private  Label song_name_label;

    @FXML
    private BorderPane songsPane;


    // media player attributes ----------------------------------------

    public static Media media;
    public static MediaPlayer mediaPlayer;

    public static File directory;
    public static File[] files;

    public static  ArrayList<File> songs;

    public static  int songNumber;
    public static int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    public static  Timer timer;
    public static  TimerTask task;

    public static  boolean running;

    public static double current_play_time;

    // pop ups---------------------------------------

    @FXML
    private ImageView album_button;

    @FXML
    private ImageView explore_button;

    @FXML
    private ImageView favorite_button;

    @FXML
    private ImageView history_button;

    @FXML
    private ImageView notification_button;

    @FXML
    private ImageView play_list_button;

    @FXML
    private ImageView premium_button;

    @FXML
    private ImageView profile_button;

    @FXML
    private ImageView share_button;

    @FXML
    private ImageView sign_out_button;

    private static Account account;
    public static void setAccount(Account account)
    {
        homepage_artist_podcaster_controller.account=account;
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        if(songs==null) {
            songs = new ArrayList<File>();

            directory = new File("src/main/resources/com/example/melodyhub/musics");

            files = directory.listFiles();

            if (files != null) {

                for (File file : files) {

                    songs.add(file);
                }

                media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                song_name_label.setText(songs.get(songNumber).getName());
                song_name_label.setWrapText(true);
            }


        }else {

            song_name_label.setText(songs.get(songNumber).getName());
            song_name_label.setWrapText(true);
            continueTimer();

        }

        // setting other classes media objects



//        for(int i = 0; i < speeds.length; i++) {
//
//            speedBox.getItems().add(Integer.toString(speeds[i])+"%");
//        }
//
//        speedBox.setOnAction(this::changeSpeed);

//        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

//            @Override
//            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
//
//                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
//            }
//        });

//         songProgressBar.setStyle("-fx-accent: #00FF00;");

    }

    public   void playMedia() {

        beginTimer();
        mediaPlayer.play();
    }

    public  void pauseMedia() {

        cancelTimer();
        mediaPlayer.pause();
    }

    public   void resetMedia() {

        play_progress_bar.setValue(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public   void previousMedia() {

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

    public  void nextMedia() {

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

//    public void get_play_time(){
//        current_play_time = mediaPlayer.getCurrentTime().toSeconds();
//
//    }

    // pop ups----------------------------------------------

    public void open_profile() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profileruser.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);

    }

    public void open_explore() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchPage.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_favorites() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("favsongs.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_history() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("history.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_notification() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notifs.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_premium() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("verification.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_share() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("share.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_playlist() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_album() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("album.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }

    public void open_signout() throws IOException {

        // Load the FXML file for the new page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signout.fxml"));
        Parent root = loader.load();

        // Create a new Scene based on the loaded FXML file
        Scene newScene = new Scene(root);

        // Get the current Stage from any component in the existing scene
        Stage currentStage = (Stage) profile_button.getScene().getWindow();

        // Set the new Scene on the Stage
        currentStage.setScene(newScene);


    }
}



