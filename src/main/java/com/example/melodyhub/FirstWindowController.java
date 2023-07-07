package com.example.melodyhub;

import com.example.melodyhub.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.melodyhub.LoginSignupPage.*;
import static org.testng.AssertJUnit.assertEquals;

public class FirstWindowController implements Initializable {

    String page;

    @FXML
    private AnchorPane authentication;

    @FXML
    private AnchorPane forgetPassword2;

    @FXML
    private AnchorPane forgetPassword1;
    @FXML
    private TextField authCode_field;
    @FXML
    private Button forgot_password_confirm_b;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField answer_field;
    @FXML
    private TextField username_field;
    @FXML
    private TextField username_field1;
    @FXML
    private TextField authCode_field1;
    @FXML
    private TextField password_field;
    @FXML
    private ComboBox<String> ques_combo;
    @FXML
    private RadioButton podcasterAccount_radio;
    @FXML
    private RadioButton userAccount_radio;
    @FXML
    private RadioButton artistAccount_radio;
    @FXML
    private Button change_btn;
    @FXML
    private TextField password_field1;
    @FXML
    private Button change_btn1;
    @FXML
    private RadioButton artistAccount_radio1;
    @FXML
    private RadioButton userAccount_radio1;
    @FXML
    private RadioButton podcasterAccount_radio1;
    @FXML
    private Button confirm_btn1;
    @FXML
    private Label newPass_label;


    public static boolean edited = false;
    public static String job;
    public static int code;

    public static void setJob(String work) {
        job = work;
    }

    public void confirmLoginAuthClicked() throws IOException {
        try {
            code = Integer.parseInt(authCode_field.getText());
            edited = true;
            back();
        } catch (Exception exception) {
            authCode_field.setText("pls enter number");
            return;
        }
    }

    public void getUserNameTotp() {
        String accType;
        if (artistAccount_radio1.isSelected()) accType = "artist";
        else if (podcasterAccount_radio1.isSelected()) accType = "podcaster";
        else if (userAccount_radio1.isSelected()) accType = "user";
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Alert");
            alert.setHeaderText("account not found");
            alert.setContentText("");

            // Show the alert
            alert.showAndWait();
            return;
        }

        sendMessage("forget pass");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("work", "TOTP");
        jsonObject.put("username", username_field1.getText());
        jsonObject.put("type", accType);
        sendMessage(jsonObject.toString());
        String msg = getMessage();
        if (msg.equals("TOTP")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Alert");
            alert.setHeaderText("code send to your email");
            alert.setContentText("");
            // Show the alert
            alert.showAndWait();
            newPass_label.setVisible(true);
            authCode_field1.setVisible(true);
            confirm_btn1.setVisible(true);
        } else if (msg.equals("account not found")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Alert");
            alert.setHeaderText("account not found");
            alert.setContentText("");

            // Show the alert
            alert.showAndWait();
        }
    }

    public void confirmForgetPassTOTP() throws IOException {

        sendMessage(authCode_field1.getText());
        if (getMessage().equals("you are you")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Alert");
            alert.setHeaderText("congratulations You are you");
            alert.setContentText("you can now set pass");
            // Show the alert
            alert.showAndWait();
//            password_field1.setVisible(true);
//            change_btn.setVisible(true);

            // Create a text field
            TextField textField = new TextField();
            textField.setLayoutX(50);
            textField.setLayoutY(50);

            // Create a button and add an event handler to it
            Button button = new Button("submit");
            button.setLayoutX(50);
            button.setLayoutY(80);
            button.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                @Override
                public void handle(javafx.event.ActionEvent actionEvent) {
                    sendMessage(textField.getText());
                    getMessage();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Alert");
                    alert.setHeaderText("you password changes");
                    alert.setContentText("");
                    ((Stage) textField.getScene().getWindow()).close();
                    ((Stage) mainPane.getScene().getWindow()).close();
                    try {
                        new LoginSignupPage().start(new Stage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Create a pane and add the text field and button to it'

            HBox HBox = new HBox();
            HBox.getChildren().addAll(textField, button);
            Stage stage = new Stage();
            // Create a scene and add the HBox to it
            Scene scene = new Scene(HBox);

            // Set the scene of the stage and show it
            stage.getIcons().add(new Image(Account.class.getResource("images/logo.jpg").toExternalForm()));
            stage.setTitle("MelOXDy hub!!");
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Alert");
            alert.setHeaderText("i'm sorry , We guess you had hard day bro \n you lost yourself , find it.");
            alert.setContentText("try more");

            // Show the alert
            alert.showAndWait();
        }
    }

    public void confirmForgetPassQues() throws IOException {
        String accType;
        if (artistAccount_radio.isSelected()) accType = "artist";
        else if (podcasterAccount_radio.isSelected()) accType = "podcaster";
        else accType = "user";

        setSocket();
        sendMessage("forget pass");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("work", "answer");
        jsonObject.put("username", username_field.getText());
        jsonObject.put("type", accType);
        jsonObject.put("answer", answer_field.getText());
        jsonObject.put("number", ques_combo.getSelectionModel().getSelectedIndex());
        sendMessage(jsonObject.toString());

        if (Objects.equals("you are you", getMessage())) {

            new Alert(Alert.AlertType.INFORMATION, "Correct answer!\nnow enter new password").show();
            password_field.setVisible(true);
            change_btn.setVisible(true);

        } else
            new Alert(Alert.AlertType.INFORMATION, "Wrong answer or question!\nTry again.").show();
    }

    public void switchChangeClicked() {
        if (Objects.equals(page, "forgetPassword1"))
            setPage("forgetPassword2");
        else if (Objects.equals(page, "forgetPassword2"))
            setPage("forgetPassword1");
    }

    public void changePasswordClicked() throws IOException {
        sendMessage(password_field1.getText());
        getMessage();

        new Alert(Alert.AlertType.INFORMATION, "Your password is updated.\n" +
                "Now you can login with your username and new password!").show();

        ((Stage) this.password_field.getScene().getWindow()).close();
        new LoginSignupPage().start(new Stage());
    }

    public void back() throws IOException {
        ((Stage) this.mainPane.getScene().getWindow()).close();
    }

    public void setPage(String pageId) {
        for (Node node : mainPane.getChildren())
            node.setVisible(Objects.equals(node.getId(), pageId));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Objects.equals(job, "authentication")) {
            authentication.setVisible(true);
            forgetPassword2.setVisible(false);
            forgetPassword1.setVisible(false);
        } else if (job == "forget pass") {
            authentication.setVisible(false);
            forgetPassword2.setVisible(true);
            forgetPassword1.setVisible(false);
        }
        ArrayList<String> s = new ArrayList<>();
        s.add("Whats your favorite app?");
        s.add("Who’s your favorite pet?");
        s.add("If you could vacation anywhere in the world, where would it be?");
        s.add("What is your favorite animal?");
        s.add("Who’s your favorite person in the whole world?");
        s.add("What is your favorite season?");
        ques_combo.getItems().addAll(s);
        ques_combo.getSelectionModel().selectFirst();
    }
}