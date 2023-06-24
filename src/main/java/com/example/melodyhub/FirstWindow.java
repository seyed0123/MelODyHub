package com.example.melodyhub;

import com.example.melodyhub.Account;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstWindow extends Application {

    private String page;

    public FirstWindow(String page) {
        this.page = page;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstWindow.class.getResource("FirstWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("welcome!");
        stage.setScene(scene);
        ((FirstWindowController) fxmlLoader.getController()).setPage(page);
        ((FirstWindowController) fxmlLoader.getController()).page = page;
        //((FirstWindowController) fxmlLoader.getController()).setQues_combo();
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}