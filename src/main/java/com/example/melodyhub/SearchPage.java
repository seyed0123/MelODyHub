package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchPage extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SearchPage.class.getResource("SearchPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
        stage.setTitle("MelOXDy hub!!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}