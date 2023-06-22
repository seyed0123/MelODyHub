package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.melodyhub.LoginSignupPage.*;

public class HomeController implements Initializable {
    private static User user;
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
    private Label singerNameLabel;

    @FXML
    private Label songNameLabel;

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
                        Image image = new Image("@images/covers/default.png");
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
                            System.out.println("VBox clicked");
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
                        Image image = new Image("G:\\code\\java\\MelodyHub\\src\\main\\resources\\com\\example\\melodyhub\\images\\covers\\default.png");
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
                            System.out.println("VBox clicked");
                        }
                    });
                    this.recom.getChildren().addAll(vbox);
                }
                explore.setOnMouseClicked(event -> {
                    System.out.println("Explore clicked");
                });

                favs.setOnMouseClicked(event -> {
                    System.out.println("Favs clicked");
                });

                history.setOnMouseClicked(event -> {
                    System.out.println("History clicked");
                });

                likeImage.setOnMouseClicked(event -> {
                    System.out.println("Like image clicked");
                });

                lyricsImage.setOnMouseClicked(event -> {
                    System.out.println("Lyrics image clicked");
                });

                notif.setOnMouseClicked(event -> {
                    System.out.println("Notif clicked");
                });

                playlist.setOnMouseClicked(event -> {
                    System.out.println("Playlist clicked");
                });

                this.popular.setOnMouseClicked(event -> {
                    System.out.println("Popular clicked");
                });

                premium.setOnMouseClicked(event -> {
                    System.out.println("Premium clicked");
                });

                profile.setOnMouseClicked(event -> {
                    System.out.println("Profile clicked");
                });

                queueImage.setOnMouseClicked(event -> {
                    System.out.println("Queue image clicked");
                });

                recommended.setOnMouseClicked(event -> {
                    System.out.println("Recommended clicked");
                });

                share.setOnMouseClicked(event -> {
                    System.out.println("Share clicked");
                });

                signOut.setOnMouseClicked(event -> {
                    System.out.println("Sign out clicked");
                });

                commentImage.setOnMouseClicked(event -> {
                    System.out.println("Comment image clicked");
                });
            }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}