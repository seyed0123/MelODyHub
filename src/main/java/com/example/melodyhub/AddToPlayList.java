
package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddToPlayList extends Application {

    private final Song song;

    public AddToPlayList(Song song) {
        this.song = song;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddToPlayList.class.getResource("AddToPlaylist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Add song to playlist");
        stage.setScene(scene);
        //((AddToPlaylistController) fxmlLoader.getController()).setPageContent(song);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
