package com.example.melodyhub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.*;

import static com.example.melodyhub.LoginSignupPage.*;
import static com.example.melodyhub.homepage_artist_podcaster_controller.*;

public class UserProfilePageController implements Initializable {

    @FXML
    private Label acctype_label;

    @FXML
    private Label age_label;

    @FXML
    private Label email_label;

    @FXML
    private Label followers_label;

    @FXML
    private Label followings_label;

    @FXML
    private Label gender_label;

    @FXML
    private Label phone_label;

    @FXML
    private ListView<HBox> playlists_listview;

    @FXML
    private ImageView profile_img;

    @FXML
    private Label username_label;

    @FXML
    private ListView<Label> followings_listview;

    @FXML
    private ListView<Label> followers_listview;

    private static User user;
    @FXML
    private Label song_name_label;

    @FXML
    private Slider play_progress_bar;

    @FXML
    private ImageView home_button;

    public static void setUser(User user) {
        UserProfilePageController.user = user;
    }

    private List<Label> getFollowings() throws IOException {
        sendMessage("get followings");
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<Label> followings = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            json = getMessage();
            User user = objectMapper.readValue(json, User.class);
            Label label = new Label();
            label.setText(user.getUsername());
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("following");
                }
            });
            followings.add(label);
        }

        return followings;
    }

    private List<Label> getFollowers() throws IOException {
        sendMessage("get followers");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<Label> followers = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            objectMapper = new ObjectMapper();
            json = getMessage();
            User user = objectMapper.readValue(json, User.class);
            Label label = new Label();
            label.setText(user.getUsername());
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("followers ");
                }
            });
            followers.add(label);
        }

        return followers;
    }

    private List<HBox> getPlaylists() throws IOException {
        sendMessage("get playlists");
        String json = getMessage();
        ArrayList<UUID> uuidList = objectMapper.readValue(json, new TypeReference<>() {
        });

        List<HBox> playLists = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get playlist");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            objectMapper = new ObjectMapper();
            json = getMessage();
            PlayList playlist = objectMapper.readValue(json, PlayList.class);
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
            Label durationLabel = new Label(playlist.getDuration()+"");
            Label personalLabel = new Label(playlist.isPersonal()+"");
            Hbox.getChildren().addAll(playlistLabel, durationLabel, personalLabel);
            Hbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Stage stage = (Stage) song_name_label.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("playlist.fxml"));
                    PlayList_controller.setPlayList(playlist,user);
                    Scene scene = null;
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
            playLists.add(Hbox);
        }
        return playLists;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UUID id = user.getId();
        File file = new File("src/main/resources/com/example/melodyhub/images/profile/"+id+".png");
        try {
            if (!file.exists()) {
                sendMessage("upload image");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",id);
                sendMessage(jsonObject.toString());
                String response = getMessage();
                if(response.equals("sending cover"))
                {
                    Thread thread =new Thread(() -> downloadImage(socket,user.getId().toString()));
                    thread.start();
                    thread.join();
                }else
                {
                    throw new Exception();
                }
            }
            Image image = new Image(Account.class.getResource("images/covers/profile/"+id+".png").toExternalForm());
            profile_img.setImage(image);
        }catch (Exception e) {
            try{
                Image image = new Image(Account.class.getResource("images/profile_default.png").toExternalForm());
                profile_img.setImage(image);
            }catch (Exception ep)
            {
                ep.printStackTrace();
            }
        }
        username_label.setText(user.getUsername());
        acctype_label.setText(user.isPremium() ? "Premium" : "Normal");
        age_label.setText(String.valueOf(user.getAge()));
        phone_label.setText(user.getPhoneNumber());
        email_label.setText(user.getEmail());
        gender_label.setText(user.getGender());

        try {
            List<Label> following = getFollowings();
            List<Label> follower = getFollowers();
            followers_label.setText(follower.size()+"");
            followings_label.setText(following.size()+"");
            playlists_listview.getItems().addAll(getPlaylists());
            followings_listview.getItems().addAll(following);
            followers_listview.getItems().addAll(follower);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        song_name_label.setText(songs.get(songNumber).getName());
        song_name_label.setWrapText(true);
        continueTimer();
    }
    @FXML
    public void upload_profile(ActionEvent event) {

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
    public void playMedia() {

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