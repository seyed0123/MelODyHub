package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.melodyhub.homepage_artist_podcaster_controller.*;
import static com.example.melodyhub.LoginSignupPage.*;
public class PlayList_controller implements Initializable {


    @FXML
    private Label artist_name_song;

    @FXML
    private ImageView banner;

    @FXML
    private static Label current_song_genre;

    @FXML
    private ImageView current_song_image;

    @FXML
    private static Label current_song_label;

    @FXML
    private static Label current_song_year;

    @FXML
    private ImageView home_button;

    @FXML
    private ListView<HBox> listSong;

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
    private Label playlist_duration;

    @FXML
    private Label playlist_name;

    @FXML
    private Label playlist_publicity;

    @FXML
    private ImageView previous_track;

    @FXML
    private VBox side_bar;

    @FXML
    private Label song_name_label;

    @FXML
    private ImageView play_all_button;

    @FXML
    private BorderPane songsPane;
    private static PlayList playlist;
    private static ArrayList<Song> playlistSong;
    private static User user;

    private static String song_id;

    private ArrayList<String> playlistSongs;

    // for playing the playlist


    public static void setPlayList(PlayList playList,User user) {
        PlayList_controller.playlist = playList;
        PlayList_controller.user=user;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        {
            song_name_label.setText(songs.get(songNumber).getName());
            song_name_label.setWrapText(true);
            play_progress_bar.setValue(current_play_time);
            continueTimer();
        }
        playlist_name.setText(playlist.getName());
        playlist_duration.setText(playlist.getDuration()+"");
        playlist_publicity.setText(playlist.isPersonal()+"");
        sendMessage("get songs playlist");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playlist",playlist.getId());
        sendMessage(jsonObject.toString());
        try {
            playlistSongs = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
            for(String uuid :playlistSongs)
            {
                sendMessage("get song");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id",uuid);
                sendMessage(jsonObject1.toString());
                UUID id = user.getId();
                Song song = objectMapper.readValue(getMessage(),Song.class);
                HBox hBox = new HBox();

                Label songNameLabel = new Label(song.getName());
                songNameLabel.setTextFill(Color.DARKMAGENTA);
                songNameLabel.setFont(new Font("Arial Nova", 16.0));

                Label singerNameLabel = new Label(song.getGenre());
                singerNameLabel.setTextFill(Color.DARKMAGENTA);
                singerNameLabel.setFont(new Font("Arial Nova Light", 11.0));



                hBox.getChildren().addAll( songNameLabel, singerNameLabel);
                hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Image image = null;
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
                                    Thread thread =new Thread(() -> downloadImage(socket,"src/main/resources/com/example/melodyhub/images/profile/"+song.getId()+".png"));
                                    thread.start();
                                    thread.join();
                                }else
                                {
                                    throw new Exception();
                                }
                            }
                             image = new Image(Account.class.getResource("images/profile/"+id+".png").toExternalForm());
                            imageView.setImage(image);
                        }catch (Exception e) {
                            try{
                                 image = new Image(Account.class.getResource("images/default.png").toExternalForm());
                                imageView.setImage(image);
                            }catch (Exception ep)
                            {
                                ep.printStackTrace();
                            }
                        }
                        current_song_genre.setText(song.getGenre());
                        current_song_label.setText(song.getName());
                        current_song_image.setImage(image);
                        current_song_year.setText(song.getYear()+"");
                    }
                });
                listSong.getItems().add(hBox);
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

    // pop up --------------------------------

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
    @FXML
    void download_songs()
    {
        songs = new ArrayList<File>();
        for(String song :playlistSongs)
        {
            File file = new File("src/main/resources/com/example/melodyhub/musics/"+song+".mp3");
            if(!file.exists())
            {
                sendMessage("upload song");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",song);
                sendMessage(jsonObject.toString());
                    downloadSong(socket,"src/main/resources/com/example/melodyhub/musics/"+song+".mp3");
            }
            songs.add(file);
        }
        songNumber=0;
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        song_name_label.setText(songs.get(songNumber).getName());
        song_name_label.setWrapText(true);
    }

    @FXML
    void play_all(){

        download_songs();
    }


}
