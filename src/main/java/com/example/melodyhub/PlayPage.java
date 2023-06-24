package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlayPage extends Application {

    private final List<Song> playlist;
    private final Song song;

    public PlayPage(List<Song> playlist, Song song) {
        this.playlist = playlist;
        this.song = song;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PlayPage.class.getResource("PlayPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Play Song");
//        ((PlayPageController) fxmlLoader.getController()).setPageContent(playlist, song);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}