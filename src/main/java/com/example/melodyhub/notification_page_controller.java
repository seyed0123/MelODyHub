package com.example.melodyhub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class notification_page_controller implements Initializable {

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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }


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
