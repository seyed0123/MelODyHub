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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
import java.util.Base64;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.melodyhub.LoginSignupPage.*;
import static org.testng.AssertJUnit.assertEquals;

public class FirstWindowController implements Initializable {

    static Account loginAccount;
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

    public static boolean edited = false;
    public static String job;
    public static int code;

    public static void setJob(String work)
    {
        job=work;
    }
    public void confirmLoginAuthClicked() throws IOException {
            try {
                code = Integer.parseInt(authCode_field.getText());
                edited=true;
                back();
            }catch (Exception exception) {
                authCode_field.setText("pls enter number");
                return;
            }
    }

    public void confirmForgetPassTOTP() throws IOException {
        String accType;
        if (artistAccount_radio.isSelected()) accType = "artist";
        else if (podcasterAccount_radio.isSelected()) accType = "podcaster";
        else accType = "user";

        setSocket();
        sendMessage("forget pass");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("work", "TOTP");
        jsonObject.put("username", username_field1.getText());
        jsonObject.put("type", accType);
        sendMessage(jsonObject.toString());

        sendMessage(authCode_field1.getText());
        assertEquals("TOTP", getMessage());

        if ("you are you".equals(getMessage())) {
            new Alert(Alert.AlertType.INFORMATION, "Authenticated Successfully!\n" +
                    "Now enter a new password.").show();
            password_field1.setVisible(true);
            change_btn1.setVisible(true);
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Wrong Code!").show();
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
        String newPass = password_field.getText();
        sendMessage(newPass);

        if (Objects.equals("password updated", getMessage())) {

            new Alert(Alert.AlertType.INFORMATION, "Your password is updated.\n" +
                    "Now you can login with your username and new password!").show();

            ((Stage) this.password_field.getScene().getWindow()).close();
            new LoginSignupPage().start(new Stage());

        }
    }

    public void back() throws IOException {
        ((Stage) this.password_field.getScene().getWindow()).close();
    }

    public void setPage(String pageId) {
        for (Node node : mainPane.getChildren())
            node.setVisible(Objects.equals(node.getId(), pageId));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Objects.equals(job, "authentication"))
        {
            authentication.setVisible(true);
            forgetPassword2.setVisible(false);
            forgetPassword1.setVisible(false);
        }
    }
}