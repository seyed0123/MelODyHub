package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SongsListPage extends Application {

    private String genre;

    public SongsListPage(String genre) {
        this.genre = genre;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SongsListPage.class.getResource("SongsListPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Songs");
        stage.setScene(scene);
        ((SongsListController)fxmlLoader.getController()).fillList(genre);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}