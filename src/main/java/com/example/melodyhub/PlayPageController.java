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

    @FXML
    private Slider play_progress_bar;

    @FXML
    public Label song_name_label;


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

                ImageView imageView = new ImageView();
                imageView.setFitHeight(100.0);
                imageView.setFitWidth(100.0);
                imageView.setPickOnBounds(true);
                imageView.setPreserveRatio(true);
                // Create the VBox
                HBox Hbox = new HBox();
                Hbox.setAlignment(Pos.CENTER_LEFT);
                Hbox.setSpacing(10);
                Hbox.setPadding(new Insets(10));
                Hbox.getChildren().add(imageView);

                // downloading cover

                try {
                    File file1 = new File("src/main/resources/com/example/melodyhub/images/profile/"+song.getId()+".png");
                    if (!file1.exists()) {
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
                    Image cover_image = new Image(Account.class.getResource("images/profile/" + fileName + ".png").toExternalForm());
                    imageView.setImage(cover_image);
                } catch (Exception e) {
                    try {
                        Image cover_image = new Image(Account.class.getResource("images/default.png").toExternalForm());
                        imageView.setImage(cover_image);
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
        setCurrent();
    }

    private void setCurrent()
    {
        try {
            songImage.setImage(new Image(Account.class.getResource("images/profile/" + current_song_id + ".png").toExternalForm()));
        }catch (Exception e)
        {songImage.setImage(new Image(Account.class.getResource("images/default.png").toExternalForm()));}
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


