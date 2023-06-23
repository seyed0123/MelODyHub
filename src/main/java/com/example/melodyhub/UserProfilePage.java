package com.example.melodyhub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserProfilePage extends Application {

    private final User user;

    public UserProfilePage(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserProfilePage.class.getResource("profile_user.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("User Profile");
        stage.setScene(scene);
        ((UserProfilePageController)fxmlLoader.getController()).setUser(user);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}