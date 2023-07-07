package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SongsListPage extends Application {

    private final List<Song> songList;

    public SongsListPage(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SongsListPage.class.getResource("SongsListPage.fxml"));
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