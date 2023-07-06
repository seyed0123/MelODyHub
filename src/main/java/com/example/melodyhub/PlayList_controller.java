package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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

import static com.example.melodyhub.HomeController.*;
import static com.example.melodyhub.HomeController.current_song_name;
import static com.example.melodyhub.homepage_artist_podcaster_controller.*;
import static com.example.melodyhub.LoginSignupPage.*;
public class PlayList_controller implements Initializable {


    @FXML
    private Label artist_name_song;

    @FXML
    private ImageView banner;

    @FXML
    private  Label current_song_genre;

    @FXML
    private ImageView current_song_image;

    @FXML
    private  Label current_song_label;

    @FXML
    private  Label current_song_year;

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

    @FXML
    private Button smartShuffle;
    @FXML
    private Button switchOrder;
    @FXML
    private Button removeSong;
    @FXML
    private Button shuffle;
    @FXML
    private Button changeOrder;
    @FXML
    private Button like;
    @FXML
    private Button owners;
    @FXML
    private Button delete;


    public static void setPlayList(PlayList playList,User user) {
        PlayList_controller.playlist = playList;
        PlayList_controller.user=user;
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
            current_song_duration = String.valueOf(song.getDuration());
            current_song_rate = String.valueOf(song.getRate());
            current_song_lyrics = song.getLyrics();
            song_name_label.setText(current_song_name);
            song_name_label.setWrapText(true);

            continueTimer();
        }
        sendMessage("get favorite playlist");
        ArrayList<UUID> favorite  = null;
        try {
            favorite = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<UUID>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(favorite.contains(playlist.getId()))
            like.setText("dislike");
        playlist_name.setText(playlist.getName());
        playlist_duration.setText(playlist.getDuration()+"");
        playlist_publicity.setText(playlist.isPersonal()+"");
        sendMessage("get songs playlist");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playlist",playlist.getId());
        sendMessage(jsonObject.toString());
        try {
            playlistSongs = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
            setSongs(playlistSongs);
            like.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(Objects.equals(like.getText(), "like"))
                    {
                        sendMessage("add favorite playlist");
                        JSONObject jsonObject1 =  new JSONObject();
                        jsonObject1.put("playlist",playlist.getId());
                        sendMessage(jsonObject1.toString());
                        like.setText("dislike");
                    }else
                    {
                        sendMessage("remove favorite playlist");
                        JSONObject jsonObject1 =  new JSONObject();
                        jsonObject1.put("playlist",playlist.getId());
                        sendMessage(jsonObject1.toString());
                        like.setText("like");
                    }
                }
            });
            shuffle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Collections.shuffle(playlistSongs);
                    setSongs(playlistSongs);
                }
            });
            smartShuffle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    sendMessage("smart shuffle");
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("playlist",playlist.getId());
                    sendMessage(jsonObject1.toString());
                    try {
                        playlistSongs = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    setSongs(playlistSongs);
                }
            });
            removeSong.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage stage = new Stage();
                    Label numberLabel = new Label("Enter a song number:");
                    TextField numberField = new TextField();

                    // Create a button to retrieve the input value
                    Button submitButton = new Button("Submit");
                    submitButton.setOnAction(e -> {
                        int number = Integer.parseInt(numberField.getText())-1;
                        sendMessage("remove song playlist");
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("playlist",playlist.getId());
                        jsonObject1.put("song",playlistSongs.get(number));
                        sendMessage(jsonObject1.toString());
                        refresh();
                        stage.close();
                    });

                    // Create a layout for the input and button
                    VBox inputLayout = new VBox(10, numberLabel, numberField, submitButton);
                    Scene resultScene = new Scene(inputLayout);

                    // Set the scene on the primary stage
                    stage.setScene(resultScene);
                    stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
                    stage.setTitle("MelOXDy hub!!");
                    stage.show();
                }
            });
            switchOrder.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage stage = new Stage();
                    TextField numberField = new TextField();
                    numberField.setPromptText("song 1");
                    TextField numberField1 = new TextField();
                    numberField1.setPromptText("song 2");

                    // Create a button to retrieve the input value
                    Button submitButton = new Button("Submit");
                    submitButton.setOnAction(e -> {
                        int number1 = Integer.parseInt(numberField.getText())-1;
                        int number2 = Integer.parseInt(numberField1.getText())-1;
                        sendMessage("change song order playlist");
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("playlist",playlist.getId());
                        jsonObject1.put("song1",playlistSongs.get(number1));
                        jsonObject1.put("song2",playlistSongs.get(number2));
                        sendMessage(jsonObject1.toString());
                        refresh();
                        stage.close();
                    });

                    // Create a layout for the input and button
                    VBox inputLayout = new VBox(numberField,numberField1, submitButton);
                    Scene resultScene = new Scene(inputLayout);

                    // Set the scene on the primary stage
                    stage.setScene(resultScene);
                    stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
                    stage.setTitle("MelOXDy hub!!");
                    stage.show();
                }
            });
            changeOrder.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage stage = new Stage();
                    TextField numberField = new TextField();
                    numberField.setPromptText("song 1");
                    TextField numberField1 = new TextField();
                    numberField1.setPromptText("place that you want");

                    // Create a button to retrieve the input value
                    Button submitButton = new Button("Submit");
                    submitButton.setOnAction(e -> {
                        int number1 = Integer.parseInt(numberField.getText())-1;
                        int number2 = Integer.parseInt(numberField1.getText());
                        sendMessage("place a song in playlist");
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("playlist",playlist.getId());
                        jsonObject1.put("id",playlistSongs.get(number1));
                        jsonObject1.put("order",number2);
                        sendMessage(jsonObject1.toString());
                        refresh();
                        stage.close();
                    });

                    // Create a layout for the input and button
                    VBox inputLayout = new VBox(numberField,numberField1, submitButton);
                    Scene resultScene = new Scene(inputLayout);

                    // Set the scene on the primary stage
                    stage.setScene(resultScene);
                    stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
                    stage.setTitle("MelOXDy hub!!");
                    stage.show();
                }
            });
            owners.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage stage = new Stage();
                    ListView<HBox> listView = new ListView<>();
                    listView.setPrefSize(300, 200);

                    // Add each item to the ListView as an HBox
                    sendMessage("get owners playlist");
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("playlist",playlist.getId());
                    sendMessage(jsonObject1.toString());
                    try {
                        for (String item : objectMapper.readValue(getMessage(), new TypeReference<ArrayList<String>>() {
                        })) {
                            sendMessage("get user");
                            JSONObject object = new JSONObject();
                            object.put("id",item);
                            sendMessage(object.toString());
                            User user1 = objectMapper.readValue(getMessage(),User.class);
                            Label label = new Label(user1.getUsername());
                            HBox.setHgrow(label, Priority.ALWAYS);
                            HBox hbox = new HBox(label);
                            hbox.setPadding(new Insets(5));
                            hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Confirmation");
                                    alert.setHeaderText("Are you sure?");
                                    alert.setContentText("Do you really want to remove his/her?");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.isPresent() && result.get() == ButtonType.OK) {
                                        sendMessage("remove owner");
                                        JSONObject object = new JSONObject();
                                        object.put("playlist",playlist.getId());
                                        object.put("id",user1.getId());
                                        sendMessage(object.toString());
                                        if(!getMessage().equals("done"))
                                        {
                                            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                                            alert1.setTitle("alert");
                                            alert1.setContentText("you dont have permission");
                                            alert1.showAndWait();
                                        }
                                    } else {
                                        listView.getSelectionModel().clearSelection();
                                    }
                                }
                            });
                            listView.getItems().add(hbox);
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    // Create a layout for the ListView
                    VBox layout = new VBox(listView);
                    layout.setPadding(new Insets(10));
                    layout.setSpacing(10);

                    // Create a scene with the layout
                    Scene scene = new Scene(layout);
                    stage.setScene(scene);
                    stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
                    stage.setTitle("MelOXDy hub!!");
                    stage.show();
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sendMessage("remove playlist");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("playlist",playlist.getId());
                sendMessage(jsonObject1.toString());
                try {
                    open_home(null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
        pauseMedia();
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
        playMedia();
    }

    @FXML
    void play_all(){

        download_songs();
    }
    @FXML
    void refresh()
    {
        sendMessage("get songs playlist");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playlist",playlist.getId());
        sendMessage(jsonObject.toString());
        try {
            playlistSongs = objectMapper.readValue(getMessage(), new TypeReference<ArrayList<String>>() {
            });
            setSongs(playlistSongs);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    void setSongs(ArrayList<String> playlistSongs){
        listSong.getItems().clear();
        try {
            for (String uuid : playlistSongs) {
                sendMessage("get song");
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", uuid);
                sendMessage(jsonObject1.toString());
                UUID id = user.getId();
                Song song = objectMapper.readValue(getMessage(), Song.class);
                HBox hBox = new HBox();

                Label songNameLabel = new Label(song.getName());
                songNameLabel.setTextFill(Color.DARKMAGENTA);
                songNameLabel.setFont(new Font("Arial Nova", 16.0));


                Label singerNameLabel = new Label(song.getGenre());
                singerNameLabel.setTextFill(Color.DARKMAGENTA);
                singerNameLabel.setFont(new Font("Arial Nova Light", 11.0));


                hBox.getChildren().addAll(songNameLabel, singerNameLabel);
                hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Image image = null;
                        ImageView imageView = new ImageView();
                        imageView.setFitHeight(100.0);
                        imageView.setFitWidth(100.0);
                        imageView.setPickOnBounds(true);
                        String id = song.getId();
                        imageView.setPreserveRatio(true);
                        File file = new File("src/main/resources/com/example/melodyhub/images/profile/" + id + ".png");
                        try {
                            if (!file.exists()) {
                                sendMessage("download music cover");
                                JSONObject jsonObject2 = new JSONObject();
                                jsonObject2.put("id", id);
                                sendMessage(jsonObject2.toString());
                                String response = getMessage();
                                if (response.equals("sending cover")) {
                                    Thread thread = new Thread(() -> downloadImage(socket, "src/main/resources/com/example/melodyhub/images/profile/" + song.getId() + ".png"));
                                    thread.start();
                                    thread.join();
                                } else {
                                    throw new Exception();
                                }
                            }
                            image = new Image(Account.class.getResource("images/profile/" + id + ".png").toExternalForm());
                        } catch (Exception e) {
                            try {
                                image = new Image(Account.class.getResource("images/default.png").toExternalForm());
                            } catch (Exception ep) {
                                ep.printStackTrace();
                            }
                        }
                        current_song_genre.setText(song.getGenre());
                        current_song_label.setText(song.getName());
                        current_song_image.setImage(image);
                        current_song_year.setText(song.getYear() + "");
                    }
                });
                listSong.getItems().add(hBox);
            }
        }catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
