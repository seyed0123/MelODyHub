package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

import static com.example.melodyhub.LoginSignupPage.*;
import static com.example.melodyhub.homepage_artist_podcaster_controller.*;

public class favorite_page_controller implements Initializable {

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
    private ListView<HBox> fav_playlist_list_view;

    private ArrayList<UUID> playlistSongs;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // get the favorite playlists
        try {
            sendMessage("get playlists");
            ArrayList<UUID> playlists = objectMapper.readValue(getMessage(), new TypeReference<ArrayList<UUID>>() {
            });
            for (UUID uuid : playlists) {
                sendMessage("get playlist");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", uuid);
                sendMessage(jsonObject1.toString());
                PlayList playlist = objectMapper.readValue(getMessage(), PlayList.class);
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
                Label durationLabel = new Label(playlist.getDuration() + "");
                Label personalLabel = new Label(playlist.isPersonal() + "");
                Hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        // Create the text fields
                        TextField textField = new TextField();

                        Stage stage = new Stage();
                        // Create the VBox
                        VBox vbox = new VBox();
                        vbox.setAlignment(Pos.CENTER_LEFT);
                        vbox.setSpacing(10);
                        vbox.setPadding(new Insets(10));
                        vbox.getChildren().addAll(
                                new Label("who do you want to share with ?"),
                                textField);

                        // Create the submit button
                        Button submitButton = new Button("Submit");
                        submitButton.setOnAction(event -> {
                            String inputString = textField.getText();
                            sendMessage("share playlist");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("user",inputString);
                            jsonObject.put("playlist",playlist.getId());
                            sendMessage(jsonObject.toString());
                            if(getMessage()=="done")
                            {
                                // Create a new alert
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                                // Set the alert title and content text
                                alert.setTitle("information");
                                alert.setHeaderText(null);
                                alert.setContentText("your playlist was sent");

                                // Display the alert dialog
                                alert.showAndWait();
                            }
                            else{
                                // Create a new alert
                                Alert alert = new Alert(Alert.AlertType.WARNING);

                                // Set the alert title and content text
                                alert.setTitle("Warning");
                                alert.setHeaderText(null);
                                alert.setContentText("you don't have permission to send this playlist");

                                // Display the alert dialog
                                alert.showAndWait();
                            }
                            stage.close();
                        });

                        // Add the submit button to the VBox
                        vbox.getChildren().add(submitButton);

                        // Create the scene
                        Scene scene = new Scene(vbox);
                        stage.setScene(scene);
                        stage.show();

                    }
                });
                Hbox.getChildren().addAll(playlistLabel, durationLabel, personalLabel);
                this.fav_playlist_list_view.getItems().add(Hbox);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//         get the favorite songs
//
//        try {
//            sendMessage("get favorite playlist");
//            ArrayList<UUID> playlists = objectMapper.readValue(getMessage(), new TypeReference<ArrayList<UUID>>() {
//            });
//            for (UUID uuid : playlists) {
//                sendMessage("get playlist");
//                JSONObject jsonObject1 = new JSONObject();
//                jsonObject1.put("id", uuid);
//                sendMessage(jsonObject1.toString());
//                PlayList playlist = objectMapper.readValue(getMessage(), PlayList.class);
//                // Load the image
//                Image image = new Image(Account.class.getResource("images/default_playlist.png").toExternalForm());
//                ImageView imageView = new ImageView(image);
//                imageView.setFitHeight(100.0);
//                imageView.setFitWidth(100.0);
//                imageView.setPickOnBounds(true);
//                imageView.setPreserveRatio(true);
//                imageView.setImage(image);
//                // Create the VBox
//                HBox Hbox = new HBox();
//                Hbox.setAlignment(Pos.CENTER_LEFT);
//                Hbox.setSpacing(10);
//                Hbox.setPadding(new Insets(10));
//                Hbox.getChildren().add(imageView);
//
//                // Add the labels to the VBox
//                Label playlistLabel = new Label(playlist.getName());
//                Label durationLabel = new Label(playlist.getDuration() + "");
//                Label personalLabel = new Label(playlist.isPersonal() + "");
//                Hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent mouseEvent) {
//
//                        // Create the text fields
//                        TextField textField = new TextField();
//
//                        Stage stage = new Stage();
//                        // Create the VBox
//                        VBox vbox = new VBox();
//                        vbox.setAlignment(Pos.CENTER_LEFT);
//                        vbox.setSpacing(10);
//                        vbox.setPadding(new Insets(10));
//                        vbox.getChildren().addAll(
//                                new Label("who do you want to share with ?"),
//                                textField);
//
//                        // Create the submit button
//                        Button submitButton = new Button("Submit");
//                        submitButton.setOnAction(event -> {
//                            String inputString = textField.getText();
//                            sendMessage("share playlist");
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("user",inputString);
//                            jsonObject.put("playlist",playlist.getId());
//                            sendMessage(jsonObject.toString());
//                            if(getMessage()=="done")
//                            {
//                                // Create a new alert
//                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//
//                                // Set the alert title and content text
//                                alert.setTitle("information");
//                                alert.setHeaderText(null);
//                                alert.setContentText("your playlist was sent");
//
//                                // Display the alert dialog
//                                alert.showAndWait();
//                            }
//                            else{
//                                // Create a new alert
//                                Alert alert = new Alert(Alert.AlertType.WARNING);
//
//                                // Set the alert title and content text
//                                alert.setTitle("Warning");
//                                alert.setHeaderText(null);
//                                alert.setContentText("you don't have permission to send this playlist");
//
//                                // Display the alert dialog
//                                alert.showAndWait();
//                            }
//                            stage.close();
//                        });
//
//                        // Add the submit button to the VBox
//                        vbox.getChildren().add(submitButton);
//
//                        // Create the scene
//                        Scene scene = new Scene(vbox);
//                        stage.setScene(scene);
//                        stage.show();
//
//                    }
//                });
//                Hbox.getChildren().addAll(playlistLabel, durationLabel, personalLabel);
//                this.fav_playlist_list_view.getItems().add(Hbox);
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        // media playing functionality

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



    // function for going back to the home page
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

}
