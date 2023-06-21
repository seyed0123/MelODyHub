package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SongsListPage extends Application {

    private String genre;
    private List<Song> songList;

    public SongsListPage(String genre, List<Song> songList) {
        this.genre = genre;
        this.songList = songList;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SongsListPage.class.getResource("SongsListPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Songs");
        stage.setScene(scene);
        ((SongsListController) fxmlLoader.getController()).songList = songList;
        ((SongsListController) fxmlLoader.getController()).fillList(genre);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}