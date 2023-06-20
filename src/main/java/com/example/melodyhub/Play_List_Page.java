package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Play_List_Page extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("playlist.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login / Signup");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        //setSocket();
        launch(args);
    }

}
