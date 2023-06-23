package com.example.melodyhub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class premium {
    @FXML
    void play(ActionEvent event) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("wt.exe", "-d", ".", "docker", "run", "--rm", "-it", "oxdman");
        processBuilder.directory(new File("."));
        processBuilder.redirectErrorStream(true);

// Start process
        Process process = processBuilder.start();

        process.waitFor();
        LoginSignupPage.sendMessage("update premium");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("column","premium");
        jsonObject.put("premium",true);
        LoginSignupPage.sendMessage(jsonObject.toString());
        // Get a reference to the stage
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        // Close the stage
        stage.close();
    }
}
