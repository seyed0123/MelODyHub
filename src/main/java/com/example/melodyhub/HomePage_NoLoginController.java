package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.Stack;

import static com.example.melodyhub.LoginSignupPage.*;


public class HomePage_NoLoginController implements Initializable {
    @FXML
    private ImageView banner;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane play_bar;

    @FXML
    private HBox popular;

    @FXML
    private HBox recomm;

    @FXML
    private VBox recommended;

    @FXML
    private VBox side_bar;

    @FXML
    private BorderPane songsPane;

    @FXML
    void login(MouseEvent event) throws IOException {
        Stage l = (Stage) recommended.getScene().getWindow();
        l.close();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Account.class.getResource("LoginSignupPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login / Signup");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void serach(MouseEvent event) throws IOException {
        Stage stage = (Stage) popular.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(SearchPage_NoLogin.class.getResource("SearchPage_nologin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Explore");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendMessage("get popular");
        ArrayList<String> popular = null;
        try {
            popular = objectMapper.readValue(getMessage(),new TypeReference<ArrayList<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for(String id :popular)
        {
            sendMessage("get song");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            sendMessage(jsonObject.toString());
            Song song = null;
            try {
                song = objectMapper.readValue(getMessage(), Song.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
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
                        Thread thread =new Thread(() -> downloadImage(socket,"src/main/resources/com/example/melodyhub/images/profile/"+id+".png"));
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
            this.popular.getChildren().addAll(vbox);
        }
    }
}