package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.json.JSONObject;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.melodyhub.LoginSignupPage.*;
import static com.example.melodyhub.homepage_artist_podcaster_controller.*;

public class HomeController implements Initializable {
    public static User user;
    @FXML
    private ImageView banner;

    @FXML
    private ImageView commentImage;

    @FXML
    private HBox explore;

    @FXML
    private HBox favs;

    @FXML
    private HBox history;

    @FXML
    private ImageView likeImage;

    @FXML
    private ImageView lyricsImage;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ImageView nextButton;

    @FXML
    private HBox notif;

    @FXML
    private ImageView pauseButton;

    @FXML
    private ImageView perButton;

    @FXML
    private ImageView playButton;

    @FXML
    private Pane play_bar;

    @FXML
    private Slider play_progress_bar;

    @FXML
    private HBox playlist;

    @FXML
    private HBox popular;

    @FXML
    private HBox premium;

    @FXML
    private HBox profile;

    @FXML
    private ImageView queueImage;

    @FXML
    private HBox recom;

    @FXML
    private VBox recommended;

    @FXML
    private HBox share;

    @FXML
    private VBox side_bar;

    @FXML
    private HBox signOut;

    @FXML
    public Label singerNameLabel;

    @FXML
    public Label song_name_label;

    public static String current_song_name ;
    public static String current_song_artist;
    public static String current_song_genre;
    public static String current_song_duration;
    public static String current_song_rate;
    public static String current_song_year;

    public static String current_song_id;

    public static String current_song_lyrics;
    @FXML
    private BorderPane songsPane;

    protected void onHelloButtonClick() {
        java.awt.Label welcomeText = null;
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    public static void setUser(User user)
    {
        HomeController.user=user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            sendMessage("get recommend");
            ArrayList<String> recom = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
            for(String id :recom)
            {
                    sendMessage("get song");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",id);
                    sendMessage(jsonObject.toString());
                    Song song = objectMapper.readValue(getMessage(),Song.class);
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

                    File file = new File("src/main/resources/com/example/melodyhub/images/covers/"+id+".png");
                    try {
                        if (!file.exists()) {
                            sendMessage("download music cover");
                            sendMessage(jsonObject.toString());
                            String response = getMessage();
                            if(response.equals("sending cover"))
                            {
                                Thread thread =new Thread(() -> downloadImage(socket,song.getId()));
                                thread.start();
                                thread.join();
                            }else
                            {
                                throw new Exception();
                            }
                        }
                        Image image = new Image("@images/covers/"+id+".png");
                        imageView.setImage(image);
                    }catch (Exception e) {
                        Image image = new Image(Account.class.getResource("images/default.png").toExternalForm());
                        imageView.setImage(image);
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
                    vbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("song clicked");
                        }
                });
                    this.recom.getChildren().addAll(vbox);
                }
                sendMessage("get popular");
                ArrayList<String> popular = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
                for(String id :popular)
                {
                    sendMessage("get song");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",id);
                    sendMessage(jsonObject.toString());
                    Song song = objectMapper.readValue(getMessage(),Song.class);
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
                                Thread thread =new Thread(() -> downloadImage(socket,"src/main/resources/com/example/melodyhub/images/profile/"+song.getId()+".png"));
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
                    vbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Stage stage = new Stage();
                            FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("AddToPlaylist.fxml"));
                            Scene scene = null;
                            AddToPlaylistController.setAccount(song,user);
                            try {
                                scene = new Scene(fxmlLoader.load());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            stage.setTitle("Login / Signup");
                            stage.setScene(scene);
                            stage.show();
                        }
                    });
                    this.popular.getChildren().addAll(vbox);
                }
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

                        Song song = objectMapper.readValue(getMessage(), Song.class);

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


                }else {


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
                explore.setOnMouseClicked(event -> {
                    Stage stage = (Stage) likeImage.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("SearchPage.fxml"));
                    Scene scene = null;
                    SearchPageController.setType(true);
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Login / Signup");
                    stage.setScene(scene);
                    stage.show();
                });

                favs.setOnMouseClicked(event -> {

                    // Load the FXML file for the new page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("favsongs.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Create a new Scene based on the loaded FXML file
                    Scene newScene = new Scene(root);

                    // Get the current Stage from any component in the existing scene
                    Stage currentStage = (Stage) signOut.getScene().getWindow();

                    // Set the new Scene on the Stage
                    currentStage.setScene(newScene);
                });

                history.setOnMouseClicked(event -> {
                    Stage stage = (Stage) likeImage.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("history.fxml"));
                    Scene scene = null;
                    history_page_controller.setType(true);
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Login / Signup");
                    stage.setScene(scene);
                    stage.show();
                });

                likeImage.setOnMouseClicked(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Are you sure you want to like this song?");
                    alert.setContentText("This action cannot be undone.");

                    // Show the alert and wait for the user's response
                    Optional<ButtonType> result = alert.showAndWait();

                    // Check the user's response
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        sendMessage("like song");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("song",current_song_id);
                        sendMessage(jsonObject.toString());
                    } else {
                        // The user clicked "Cancel" or closed the dialog
                        // Do nothing or handle the cancelation here
                    }
                });

                lyricsImage.setOnMouseClicked(event -> {
                    TextArea textArea = new TextArea();
                    textArea.setText(current_song_lyrics);
                    textArea.setEditable(false);
                    VBox root = new VBox(textArea);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                });

                notif.setOnMouseClicked(event -> {
                    Stage stage = (Stage) likeImage.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("notifs.fxml"));
                    Scene scene = null;
                    notification_page_controller.setUser(user);
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Login / Signup");
                    stage.setScene(scene);
                    stage.show();
                });

                playlist.setOnMouseClicked(event -> {
                    // Load the FXML file for the new page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Create a new Scene based on the loaded FXML file
                    Scene newScene = new Scene(root);

                    // Get the current Stage from any component in the existing scene
                    Stage currentStage = (Stage) signOut.getScene().getWindow();

                    // Set the new Scene on the Stage
                    currentStage.setScene(newScene);
                });

                premium.setOnMouseClicked(event -> {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("premium.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            user.setPremium(true);
                            sendMessage("update user");
                            HashMap<String,String> command = new HashMap<>();
                            command.put("premium","false");
                        }
                    });
                    stage.setTitle("Login / Signup");
                    stage.setScene(scene);
                    stage.show();
                });

                profile.setOnMouseClicked(event -> {
                    Stage stage = (Stage) likeImage.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("profile_user.fxml"));
                    UserProfilePageController.setUser(user);
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Login / Signup");
                    stage.setScene(scene);
                    stage.show();
                });

                queueImage.setOnMouseClicked(event -> {
                    FXMLLoader loader=null;

                    loader = new FXMLLoader(getClass().getResource("PlayPage.fxml"));

                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Create a new Scene based on the loaded FXML file
                    Scene newScene = new Scene(root);

                    // Get the current Stage from any component in the existing scene
                    Stage currentStage = (Stage) queueImage.getScene().getWindow();

                    // Set the new Scene on the Stage
                    currentStage.setScene(newScene);

                });


                share.setOnMouseClicked(event -> {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("share.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Login / Signup");
                    stage.setScene(scene);
                    stage.show();
                });

                signOut.setOnMouseClicked(event -> {
                    System.out.println("Sign out clicked");
                });

                commentImage.setOnMouseClicked(event -> {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("comments.fxml"));
                    Scene scene = null;
                    Comment.setSong(current_song_id);
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Login / Signup");
                    stage.setScene(scene);
                    stage.show();
                });
            }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

    public void open_queue() throws IOException {



    }
    @FXML
    public void chat()
    {
        WebView webView = new WebView();
        webView.getEngine().load("https://web.eitaa.com/");
        StackPane root = new StackPane();
        root.getChildren().add(webView);
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}