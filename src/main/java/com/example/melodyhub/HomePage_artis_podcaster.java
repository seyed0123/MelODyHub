package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage_artis_podcaster extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("HomePage_artist&podcater.fxml"));
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
