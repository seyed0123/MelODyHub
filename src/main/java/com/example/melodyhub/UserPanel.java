package com.example.melodyhub;

import com.example.melodyhub.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserPanel extends Application {

    private final User user;
    private final String page;

    public UserPanel(User user, String page) {
        this.user = user;
        this.page = page;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("UserDark.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((UserPanelController) fxmlLoader.getController()).userLogin = user;
        ((UserPanelController) fxmlLoader.getController()).setPage(page);
        stage.setTitle("User Panel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
