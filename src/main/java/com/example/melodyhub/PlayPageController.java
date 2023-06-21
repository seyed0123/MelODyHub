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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PlayPageController implements Initializable {


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

    // media player attributes

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;

    private boolean running;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        songs = new ArrayList<File>();

        directory = new File("src/main/resources/com/example/melodyhub/musics");

        files = directory.listFiles();

        if(files != null) {

            for(File file : files) {

                songs.add(file);
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        song_name_label.setText(songs.get(songNumber).getName());
        song_name_label.setWrapText(true);

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

    public void cancelTimer() {

        running = false;
        timer.cancel();
    }

    public void set_play_time(){

        double playbackPosition = play_progress_bar.getValue();
        mediaPlayer.seek(Duration.seconds(playbackPosition));
        playMedia();

    }


}
