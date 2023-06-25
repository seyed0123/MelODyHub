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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static com.example.melodyhub.LoginSignupPage.*;
import static com.example.melodyhub.homepage_artist_podcaster_controller.*;

public class UserProfilePageController implements Initializable {

    @FXML
    private ImageView storyImage;
    @FXML
    private Button uploadButton;
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

    private static Account account;
    @FXML
    private Label song_name_label;

    @FXML
    private Slider play_progress_bar;

    @FXML
    private ImageView home_button;

    public static void setUser(User user) {
        UserProfilePageController.user = user;
    }

    public static void setArtist(Account account)
    {
        UserProfilePageController.account = account;
        UserProfilePageController.user = null;
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
                    Stage stage = (Stage) phone_label.getScene().getWindow();
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
                    Stage stage = (Stage) phone_label.getScene().getWindow();
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
                }
            });
            followers.add(label);
        }

        return followers;
    }

    private List<HBox> getPlaylists() throws IOException {
        sendMessage("get playlists another");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id",user.getId());
        sendMessage(jsonObject1.toString());
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
        if(user!=null) {
            UUID id = user.getId();
            File file = new File("src/main/resources/com/example/melodyhub/images/profile/" + id + ".png");
            try {
                if (!file.exists()) {
                    sendMessage("upload image");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", id);
                    sendMessage(jsonObject.toString());
                    String response = getMessage();
                    if (response.equals("sending cover")) {
                        Thread thread = new Thread(() -> downloadImage(socket, "src/main/resources/com/example/melodyhub/images/profile/" + id + ".png"));
                        thread.start();
                        thread.join();
                    } else {
                        throw new Exception();
                    }
                }
                Image image = new Image(Account.class.getResource("images/profile/" + id + ".png").toExternalForm());
                profile_img.setImage(image);
            } catch (Exception e) {
                try {
                    Image image = new Image(Account.class.getResource("images/profile_default.png").toExternalForm());
                    profile_img.setImage(image);
                } catch (Exception ep) {
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

                if(user == HomeController.user) {
                    List<Label> following = getFollowings();
                    List<Label> follower = getFollowers();
                    followers_label.setText(follower.size() + "");
                    followings_label.setText(following.size() + "");
                    followings_listview.getItems().addAll(following);
                    followers_listview.getItems().addAll(follower);
                }else {
                    uploadButton.setVisible(false);
                    storyImage.setVisible(false);
                }
                playlists_listview.getItems().addAll(getPlaylists());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            UUID id = account.getId();
            File file = new File("src/main/resources/com/example/melodyhub/images/profile/" + id + ".png");
            try {
                if (!file.exists()) {
                    sendMessage("upload image");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", id);
                    sendMessage(jsonObject.toString());
                    String response = getMessage();
                    if (response.equals("sending cover")) {
                        Thread thread = new Thread(() -> downloadImage(socket, "src/main/resources/com/example/melodyhub/images/profile/" + id + ".png"));
                        thread.start();
                        thread.join();
                    } else {
                        throw new Exception();
                    }
                }
                Image image = new Image(Account.class.getResource("images/profile/" + id + ".png").toExternalForm());
                profile_img.setImage(image);
            } catch (Exception e) {
                try {
                    Image image = new Image(Account.class.getResource("images/profile_default.png").toExternalForm());
                    profile_img.setImage(image);
                } catch (Exception ep) {
                    ep.printStackTrace();
                }
            }
            username_label.setText(account.getUsername());
            phone_label.setText(account.getPhoneNumber());
            email_label.setText(account.getEmail());
        }
        song_name_label.setText(songs.get(songNumber).getName());
        song_name_label.setWrapText(true);
        continueTimer();
    }
    @FXML
    public void upload_profile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // Set initial directory
        File initialDirectory = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(initialDirectory);

        // Add file filters
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(imageFilter);

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("aya shoma sirous hastid ????");
            alert.setContentText("Please be careful!");

            alert.showAndWait();
        }else
        {
            sendMessage("download image");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",user.getId());
            sendMessage(jsonObject.toString());
            uploadImage(socket,selectedFile.getAbsolutePath());
            Path sourcePath = Paths.get(selectedFile.getAbsolutePath());
            Path targetPath = Paths.get("src/main/resources/com/example/melodyhub/images/profile/" + user.getId() + ".png");

            // Create the target directory if it doesn't exist
            File targetDir = targetPath.getParent().toFile();
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            // Copy the file
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        try {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    public void follow()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to follow it?");
        alert.setContentText("Click OK to proceed, or Cancel to abort.");

        // Get the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user clicked the OK button
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendMessage("follow");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("followId",user.getId());
            sendMessage(jsonObject.toString());
        } else {
            // User clicked Cancel, abort the operation
            // ...
        }
    }
    @FXML
    public void uploadStory() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // Set initial directory
        File initialDirectory = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(initialDirectory);

        // Add file filters
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(imageFilter);

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("aya shoma sirous hastid ????");
            alert.setContentText("Please be careful!");

            alert.showAndWait();
        }else
        {
            Path sourcePath = Paths.get(selectedFile.getAbsolutePath());
            Path targetPath = Paths.get("src/main/resources/com/example/melodyhub/images/profile/" + user.getId() + "story.png");

            // Create the target directory if it doesn't exist
            File targetDir = targetPath.getParent().toFile();
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            // Copy the file
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        try {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void showStory()
    {
        Stage stage = new Stage();
        Image image = new Image(Account.class.getResource("images/profile/" + user.getId() + "story.png").toExternalForm());

        // Create an ImageView control to display the image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);

        // Create a StackPane layout and add the ImageView to it
        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        // Create a Scene with the StackPane layout as root and set it on the Stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Image Viewer");
        stage.show();
    }
}