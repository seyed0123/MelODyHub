package com.example.melodyhub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.example.melodyhub.LoginSignupPage.*;

public class share_page_controller implements Initializable {

    @FXML
    private VBox play_list;

    @FXML
    private ListView<HBox> playlists;

    @FXML
    private ScrollPane play_list_scroll;

    private static Account account;
    private static Song song;

    public static void setAccount(Song song,Account account)
    {
        share_page_controller.song=song;
        share_page_controller.account=account;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                this.playlists.getItems().add(Hbox);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
