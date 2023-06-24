package com.example.melodyhub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.example.melodyhub.HomeController.user;
import static com.example.melodyhub.LoginSignupPage.getMessage;
import static com.example.melodyhub.LoginSignupPage.sendMessage;

public class SongsListController implements Initializable {

    @FXML
    private ListView<HBox> songs_table;

    private static List<Song> songList;

    private static List<Artist> artistList;
    private static List<User> userList;
    @FXML
    private TextField search_field;

    public static void setSongList(List<Song> songList,List<Artist> artist,List<User> users) {
        SongsListController.songList = songList;
        artistList=artist;
        userList=users;
    }

    public static void setSongList(List<Song> songList) {
        SongsListController.songList = songList;
        artistList=new ArrayList<>();
        userList =new ArrayList<>();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Song song :songList) {
            HBox hBox = new HBox();

            Label songNameLabel = new Label(song.getName());
            songNameLabel.setTextFill(Color.PEACHPUFF);
            songNameLabel.setFont(new Font("Arial Nova", 16.0));

            Label singerNameLabel = new Label(song.getGenre());
            singerNameLabel.setTextFill(Color.PEACHPUFF);
            singerNameLabel.setFont(new Font("Arial Nova Light", 11.0));
            hBox.getChildren().addAll( songNameLabel, singerNameLabel);
            hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
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
            songs_table.getItems().add(hBox);
        }
        for (Artist artist :artistList){
            HBox hBox = new HBox();

            Label songNameLabel = new Label(artist.getUsername());
            songNameLabel.setTextFill(Color.PEACHPUFF);
            songNameLabel.setFont(new Font("Arial Nova", 16.0));

            Label singerNameLabel = new Label(artist.getBio());
            singerNameLabel.setTextFill(Color.PEACHPUFF);
            singerNameLabel.setFont(new Font("Arial Nova Light", 11.0));
            hBox.getChildren().addAll( songNameLabel, singerNameLabel);
            songs_table.getItems().add(hBox);
        }
        for(User user :userList)
        {
            HBox hBox = new HBox();

            Label songNameLabel = new Label(user.getUsername());
            songNameLabel.setTextFill(Color.PEACHPUFF);
            songNameLabel.setFont(new Font("Arial Nova", 16.0));

            Label singerNameLabel = new Label(user.getAge()+"");
            singerNameLabel.setTextFill(Color.PEACHPUFF);
            singerNameLabel.setFont(new Font("Arial Nova Light", 11.0));
            hBox.getChildren().addAll( songNameLabel, singerNameLabel);
            songs_table.getItems().add(hBox);
        }
    }
    @FXML
    public void searchClicked() throws IOException {
        sendMessage("search");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", search_field.getText());
        sendMessage(jsonObject.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> listSong = objectMapper.readValue(getMessage(), new TypeReference<>() {
        });
        ArrayList<UUID> listArtist = objectMapper.readValue(getMessage(), new TypeReference<>() {
        });
        ArrayList<UUID> listUsers = objectMapper.readValue(getMessage(), new TypeReference<ArrayList<UUID>>() {
        });

        List<Song> result1 = getSongsById(listSong);
        List<Artist> result2 = getArtistById(listArtist);
        List<User> result3 = getUsersById(listUsers);

        Stage stage= ((Stage) search_field.getScene().getWindow());
        SongsListController.setSongList(result1,result2,result3);
        FXMLLoader fxmlLoader = new FXMLLoader(Account.class.getResource("SongsListPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Songs List");
        stage.setScene(scene);
        stage.show();
    }

    private List<Song> getSongsById(List<String> uuidList) throws IOException {
        List<Song> songList = new ArrayList<>();
        for (String uuid : uuidList) {
            sendMessage("get song");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = getMessage();
            Song song = objectMapper.readValue(json, Song.class);
            songList.add(song);
        }
        return songList;
    }
    private List<User> getUsersById(List<UUID> uuidList) throws IOException {
        List<User> songList = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get song");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = getMessage();
            User song = objectMapper.readValue(json, User.class);
            songList.add(song);
        }
        return songList;
    }
    private List<Artist> getArtistById(List<UUID> uuidList) throws IOException {
        List<Artist> songList = new ArrayList<>();
        for (UUID uuid : uuidList) {
            sendMessage("get user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", uuid);
            sendMessage(jsonObject.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = getMessage();
            Artist song = objectMapper.readValue(json, Artist.class);
            songList.add(song);
        }
        return songList;
    }
}
