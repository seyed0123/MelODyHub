package com.example.melodyhub;

import com.example.melodyhub.Artist;
import com.example.melodyhub.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.json.JSONObject;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.example.melodyhub.LoginSignupPage.*;

public class LoginSignupController implements Initializable {

    @FXML
    private ImageView dark_mode;
    @FXML
    private ImageView banner;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane back_btn;
    @FXML
    private Button login_btn;
    @FXML
    private Button signup_btn;
    @FXML
    private Pane mode_btn;
    @FXML
    private Label l1;
    @FXML
    private Label l2;
    @FXML
    private Label l3;
    @FXML
    private Label l4;
    @FXML
    private Label l5;
    @FXML
    private Label l6;
    @FXML
    private Label l7;
    @FXML
    private Label l8;
    @FXML
    private Label l9;
    @FXML
    private Label l10;
    @FXML
    private Label l11;
    @FXML
    private Label l31;
    @FXML
    private Label l12;
    @FXML
    private Label l13;
    @FXML
    private Label l14;
    @FXML
    private Label l20;
    @FXML
    private DatePicker birthDate;
    @FXML
    private ComboBox<String> q_combobox;
    @FXML
    private ComboBox<String> q2_combobox;
    @FXML
    private TextField username_field;
    @FXML
    private TextField s_username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private TextField c_password_field;
    @FXML
    private TextField answer_field;
    @FXML
    private TextField s_password_field;
    @FXML
    private TextField email_field;
    @FXML
    private TextField phone_field;
    @FXML
    private CheckBox showPass;
    @FXML
    private TextField v_password_field;
    @FXML
    private RadioButton male_radio;
    @FXML
    private RadioButton female_radio;
    @FXML
    private RadioButton listener_radio;
    @FXML
    private RadioButton artist_radio;
    @FXML
    private RadioButton podcaster_radio;
    @FXML
    private RadioButton u_radio;
    @FXML
    private RadioButton a_radio;
    @FXML
    private RadioButton p_radio;

    private boolean isLogin = true;
    private boolean isNightMode = false;

    @FXML
    public void loginClicked() throws Exception {

        // ---------------------------

        if (u_radio.isSelected()) {
            String username = username_field.getText();
            String pass = password_field.getText();

            sendMessage("login user");
            sendMessage(username);
            sendMessage(pass);

            if (getMessage().equals("login failed")) {
                new Alert(Alert.AlertType.INFORMATION, "Login failed! please try again.").show();
                return;
            } else {
                //((Stage) banner.getScene().getWindow()).close();
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("FirstWindow.fxml"));

                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    ((FirstWindowController) fxmlLoader.getController()).setPage("authentication");
                    ((FirstWindowController) fxmlLoader.getController()).page = "authentication";
//
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            if (FirstWindowController.edited) {
                                sendMessage(String.valueOf(FirstWindowController.code));
                                if (getMessage().equals("login OK")) {
                                    try {
                                        User user = objectMapper.readValue(getMessage(), User.class);
                                        HomeController.setUser(user);
                                        Stage stage = new Stage();
                                        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("HomePage.fxml"));
                                        Scene scene1 = new Scene(fxmlLoader.load());
                                        stage.setScene(scene1);
                                        stage.show();
                                        ((Stage) banner.getScene().getWindow()).close();
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Your authentication code is wrong!").show();
                                }
                            }
                        }
                    });
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int DELAY_MINUTES = 2;
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.schedule(() -> {
                    stage.close();
                    executor.shutdown();
                }, DELAY_MINUTES, TimeUnit.MINUTES);
            }
        } else if (a_radio.isSelected()) {
            String username = username_field.getText();
            String pass = password_field.getText();

            sendMessage("login artist");
            sendMessage(username);
            sendMessage(pass);

            if (getMessage().equals("login failed")) {
                new Alert(Alert.AlertType.INFORMATION, "Login failed! please try again.").show();
                return;
            } else {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("FirstWindow.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            if (FirstWindowController.edited) {
                                sendMessage(String.valueOf(FirstWindowController.code));
                                if (getMessage().equals("login OK")) {
                                    try {
                                        Artist user = objectMapper.readValue(getMessage(), Artist.class);
                                        homepage_artist_podcaster_controller.setAccount(user);
                                        Stage stage = new Stage();
                                        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("HomePage_artist&podcater.fxml"));
                                        Scene scene1 = new Scene(fxmlLoader.load());
                                        stage.setScene(scene1);
                                        stage.show();
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Your authentication code is wrong!").show();
                                }
                            }
                        }
                    });
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int DELAY_MINUTES = 2;
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.schedule(() -> {
                    stage.close();
                    executor.shutdown();
                }, DELAY_MINUTES, TimeUnit.MINUTES);
            }
        } else if (p_radio.isSelected()) {
            String username = username_field.getText();
            String pass = password_field.getText();

            sendMessage("login podcaster");
            sendMessage(username);
            sendMessage(pass);

            if (getMessage().equals("login failed")) {
                new Alert(Alert.AlertType.INFORMATION, "Login failed! please try again.").show();
                return;
            } else {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("FirstWindow.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            if (FirstWindowController.edited) {
                                sendMessage(String.valueOf(FirstWindowController.code));
                                if (getMessage().equals("login OK")) {
                                    try {
                                        Podcaster user = objectMapper.readValue(getMessage(), Podcaster.class);
                                        homepage_artist_podcaster_controller.setAccount(user);
                                        Stage stage = new Stage();
                                        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("HomePage_artist&podcater.fxml"));
                                        Scene scene1 = new Scene(fxmlLoader.load());
                                        stage.setScene(scene1);
                                        stage.show();

                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Your authentication code is wrong!").show();
                                }
                            }
                        }
                    });
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int DELAY_MINUTES = 2;
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.schedule(() -> {
                    stage.close();
                    executor.shutdown();
                }, DELAY_MINUTES, TimeUnit.MINUTES);
            }
        }
    }

    @FXML
    public void signupClicked() throws IOException {
        if (!Objects.equals(s_password_field.getText(), c_password_field.getText())) {
            new Alert(Alert.AlertType.ERROR, "Password dose match the confirmation!").show();
            return;
        }

        String username = s_username_field.getText();
        String pass = s_password_field.getText();
        String email = email_field.getText();
        String phone = phone_field.getText();

        if (listener_radio.isSelected()) {

            if (birthDate.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Please select your birth date.").show();
                return;
            }
            String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = EMAIL_PATTERN.matcher(email);
            if (!matcher.matches()) {
                new Alert(Alert.AlertType.ERROR, "Please enter valid email.you must use it later.").show();
                return;
            }
            String PHONE_REGEX = "^\\+(?:[0-9] ?){6,14}[0-9]$";
            Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
            Matcher phone_matcher = PHONE_PATTERN.matcher(phone);
            if (!phone_matcher.matches()) {
                new Alert(Alert.AlertType.ERROR, "Please enter valid phone number.you must use it later.").show();
                return;
            }
            String date = birthDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int selectedIndex = q_combobox.getSelectionModel().getSelectedIndex() + 1;
            String ans = answer_field.getText();
            String gender;
            if (male_radio.isSelected()) gender = "male";
            else if (female_radio.isSelected()) gender = "female";
            else {
                new Alert(Alert.AlertType.ERROR, "Please select gender.").show();
                return;
            }

            sendMessage("create user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", pass);
            jsonObject.put("phone", phone);
            jsonObject.put("email", email);
            jsonObject.put("gender", gender);
            jsonObject.put("date", date);
            jsonObject.put("quesId", selectedIndex);
            jsonObject.put("answer", ans);
            sendMessage(jsonObject.toString());

        } else if (podcaster_radio.isSelected()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", pass);
            jsonObject.put("phone", phone);
            jsonObject.put("email", email);
            int selectedIndex = q_combobox.getSelectionModel().getSelectedIndex() + 1;
            String ans = answer_field.getText();
            jsonObject.put("quesId", selectedIndex);
            jsonObject.put("answer", ans);
            sendMessage("create podcaster");
            sendMessage(jsonObject.toString());

        } else if (artist_radio.isSelected()) {
            sendMessage("create artist");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", pass);
            jsonObject.put("phone", phone);
            jsonObject.put("email", email);
            int selectedIndex = q_combobox.getSelectionModel().getSelectedIndex() + 1;
            String ans = answer_field.getText();
            jsonObject.put("quesId", selectedIndex);
            jsonObject.put("answer", ans);
            sendMessage(jsonObject.toString());

        } else {
            new Alert(Alert.AlertType.ERROR, "Please Select Account Type.").show();
            return;
        }

        if (getMessage().equals("done")) {
            new Alert(Alert.AlertType.INFORMATION, "You signed up successfully!" +
                    "\nNow you can login with your username and password.").show();
            goLogin();
        } else if (getMessage().equals("failed"))
            new Alert(Alert.AlertType.INFORMATION, "You can not sign up with this information.\nTry again!").show();
    }

    @FXML
    public void showPassword() {
        if (showPass.isSelected()) {
            v_password_field.setText(password_field.getText());
            v_password_field.toFront();
        } else {
            password_field.setText(v_password_field.getText());
            password_field.toFront();
        }
    }

    @FXML
    public void goSignup() {
        if (isLogin) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.007),
                    e -> banner.setTranslateX(banner.getTranslateX() - 5)));
            timeline.setCycleCount(117);
            timeline.play();
            //banner.setVisible(false);
            l1.setVisible(false);
            //signup_txt_btn.setVisible(false);
            banner.setFitWidth(520);

            mode_btn.setTranslateX(mode_btn.getTranslateX() + 560);
            back_btn.setTranslateX(back_btn.getTranslateX() + 490);

            artist_radio.selectedProperty().addListener(e -> {
                male_radio.setDisable(true);
                female_radio.setDisable(true);
                birthDate.setDisable(true);
            });

            podcaster_radio.selectedProperty().addListener(e -> {
                male_radio.setDisable(true);
                female_radio.setDisable(true);
                birthDate.setDisable(true);
            });

            listener_radio.selectedProperty().addListener(e -> {
                male_radio.setDisable(false);
                female_radio.setDisable(false);
                birthDate.setDisable(false);
            });

            isLogin = false;
        }
    }

    @FXML
    public void goLogin() {
        if (!isLogin) {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.007),
                    e -> banner.setTranslateX(banner.getTranslateX() + 5)
            ));
            timeline.setCycleCount(117);
            timeline.play();
            //banner.setVisible(true);
            l1.setVisible(true);
            //signup_txt_btn.setVisible(true);
            banner.setFitWidth(570);

            mode_btn.setTranslateX(mode_btn.getTranslateX() - 560);
            back_btn.setTranslateX(back_btn.getTranslateX() - 490);

            isLogin = true;
        }
    }

    @FXML
    public void back() throws IOException {
        ((Stage) banner.getScene().getWindow()).close();
        new HomePage_NoLogin().start(new Stage());
    }

    @FXML
    public void changeMode() throws FileNotFoundException {
        DropShadow dropShadow;
        if (isNightMode) {

            rootPane.setStyle("-fx-background-color: #EBECF0;");

            birthDate.getStyleClass().remove("night-mode");
            birthDate.getStyleClass().remove("night-mode-dp");
            birthDate.getStyleClass().add("light-mode");

            q_combobox.getStyleClass().remove("night-mode");
            q_combobox.getStyleClass().add("light-mode");

            mode_btn.getStyleClass().remove("night-mode");
            mode_btn.getStyleClass().add("light-mode");

            back_btn.getStyleClass().remove("night-mode");
            back_btn.getStyleClass().add("light-mode");

            username_field.getStyleClass().remove("night-mode");
            username_field.getStyleClass().add("light-mode");

            password_field.getStyleClass().remove("night-mode");
            password_field.getStyleClass().add("light-mode");

            c_password_field.getStyleClass().remove("night-mode");
            c_password_field.getStyleClass().add("light-mode");

            answer_field.getStyleClass().remove("night-mode");
            answer_field.getStyleClass().add("light-mode");

            s_username_field.getStyleClass().remove("night-mode");
            s_username_field.getStyleClass().add("light-mode");

            s_password_field.getStyleClass().remove("night-mode");
            s_password_field.getStyleClass().add("light-mode");

            email_field.getStyleClass().remove("night-mode");
            email_field.getStyleClass().add("light-mode");

            phone_field.getStyleClass().remove("night-mode");
            phone_field.getStyleClass().add("light-mode");

            login_btn.getStyleClass().remove("night-mode");
            login_btn.getStyleClass().add("light-mode");

            signup_btn.getStyleClass().remove("night-mode");
            signup_btn.getStyleClass().add("light-mode");

            dropShadow = new DropShadow(10, 2, 2, Color.valueOf("#babecc"));
            dropShadow.setInput(new DropShadow(10, -5, -5, Color.WHITE));
            username_field.setEffect(dropShadow);
            password_field.setEffect(dropShadow);
            s_username_field.setEffect(dropShadow);
            s_password_field.setEffect(dropShadow);
            c_password_field.setEffect(dropShadow);
            answer_field.setEffect(dropShadow);
            email_field.setEffect(dropShadow);
            phone_field.setEffect(dropShadow);
            birthDate.setEffect(dropShadow);
            q_combobox.setEffect(dropShadow);

            l1.setStyle("-fx-text-fill: black;");
            l2.setStyle("-fx-text-fill: black;");
            l3.setStyle("-fx-text-fill: black;");
            l4.setStyle("-fx-text-fill: black;");
            l5.setStyle("-fx-text-fill: black;");
            l6.setStyle("-fx-text-fill: black;");
            l7.setStyle("-fx-text-fill: black;");
            l8.setStyle("-fx-text-fill: black;");
            l9.setStyle("-fx-text-fill: black;");
            l10.setStyle("-fx-text-fill: black;");
            l11.setStyle("-fx-text-fill: black;");
            l12.setStyle("-fx-text-fill: black;");
            l13.setStyle("-fx-text-fill: black;");
            l14.setStyle("-fx-text-fill: black;");
            l20.setStyle("-fx-text-fill: black;");
            l31.setStyle("-fx-text-fill: black;");
            showPass.setStyle("-fx-text-fill: black;");
            male_radio.setStyle("-fx-text-fill: black;");
            female_radio.setStyle("-fx-text-fill: black;");
            listener_radio.setStyle("-fx-text-fill: black;");
            podcaster_radio.setStyle("-fx-text-fill: black;");
            artist_radio.setStyle("-fx-text-fill: black;");
            a_radio.setStyle("-fx-text-fill: black;");
            u_radio.setStyle("-fx-text-fill: black;");
            p_radio.setStyle("-fx-text-fill: black;");

            dropShadow = new DropShadow(10, -5, -5, Color.WHITE);
            dropShadow.setInput(new DropShadow(10, 5, 5, Color.valueOf("#babecc")));


            FileInputStream input = new FileInputStream("src/main/resources/com/example/melodyhub/images/dark-mode.png");
            Image image = new Image(input);
            dark_mode.setImage(image);

        } else {

            rootPane.setStyle("-fx-background-color: #272727;");

            birthDate.getStyleClass().remove("light-mode");
            birthDate.getStyleClass().add("night-mode");
            birthDate.getStyleClass().add("night-mode-dp");

            q_combobox.getStyleClass().remove("light-mode");
            q_combobox.getStyleClass().add("night-mode");

            mode_btn.getStyleClass().remove("light-mode");
            mode_btn.getStyleClass().add("night-mode");

            back_btn.getStyleClass().remove("light-mode");
            back_btn.getStyleClass().add("night-mode");

            username_field.getStyleClass().remove("light-mode");
            username_field.getStyleClass().add("night-mode");

            password_field.getStyleClass().remove("light-mode");
            password_field.getStyleClass().add("night-mode");

            c_password_field.getStyleClass().remove("light-mode");
            c_password_field.getStyleClass().add("night-mode");

            answer_field.getStyleClass().remove("light-mode");
            answer_field.getStyleClass().add("night-mode");

            s_password_field.getStyleClass().remove("light-mode");
            s_password_field.getStyleClass().add("night-mode");

            s_username_field.getStyleClass().remove("light-mode");
            s_username_field.getStyleClass().add("night-mode");

            email_field.getStyleClass().remove("light-mode");
            email_field.getStyleClass().add("night-mode");

            phone_field.getStyleClass().remove("light-mode");
            phone_field.getStyleClass().add("night-mode");

            login_btn.getStyleClass().remove("light-mode");
            login_btn.getStyleClass().add("night-mode");

            signup_btn.getStyleClass().remove("light-mode");
            signup_btn.getStyleClass().add("night-mode");

            dropShadow = new DropShadow(10, 2, 2, Color.rgb(0, 0, 0, 0.4));
            dropShadow.setInput(new DropShadow(10, -5, -5, Color.rgb(49, 49, 49, 0.4)));
            username_field.setEffect(dropShadow);
            password_field.setEffect(dropShadow);
            s_username_field.setEffect(dropShadow);
            s_password_field.setEffect(dropShadow);
            c_password_field.setEffect(dropShadow);
            answer_field.setEffect(dropShadow);
            email_field.setEffect(dropShadow);
            phone_field.setEffect(dropShadow);
            q_combobox.setEffect(dropShadow);
            birthDate.setEffect(dropShadow);

            l1.setStyle("-fx-text-fill: white;");
            l2.setStyle("-fx-text-fill: white;");
            l3.setStyle("-fx-text-fill: white;");
            l4.setStyle("-fx-text-fill: white;");
            l5.setStyle("-fx-text-fill: white;");
            l6.setStyle("-fx-text-fill: white;");
            l7.setStyle("-fx-text-fill: white;");
            l8.setStyle("-fx-text-fill: white;");
            l9.setStyle("-fx-text-fill: white;");
            l10.setStyle("-fx-text-fill: white;");
            l11.setStyle("-fx-text-fill: white;");
            l12.setStyle("-fx-text-fill: white;");
            l13.setStyle("-fx-text-fill: white;");
            l14.setStyle("-fx-text-fill: white;");
            l20.setStyle("-fx-text-fill: white;");
            l31.setStyle("-fx-text-fill: white;");
            showPass.setStyle("-fx-text-fill: white;");
            male_radio.setStyle("-fx-text-fill: white;");
            female_radio.setStyle("-fx-text-fill: white;");
            listener_radio.setStyle("-fx-text-fill: white;");
            artist_radio.setStyle("-fx-text-fill: white;");
            podcaster_radio.setStyle("-fx-text-fill: white;");
            u_radio.setStyle("-fx-text-fill: white;");
            a_radio.setStyle("-fx-text-fill: white;");
            p_radio.setStyle("-fx-text-fill: white;");


            dropShadow = new DropShadow(16, -9, -9, Color.rgb(49, 49, 49, 0.4));
            dropShadow.setInput(new DropShadow(16, 9, 9, Color.rgb(0, 0, 0, 0.4)));


            FileInputStream input = new FileInputStream("src/main/resources/com/example/melodyhub/images/light-mode.png");
            Image image = new Image(input);
            dark_mode.setImage(image);
        }

        back_btn.setEffect(dropShadow);
        mode_btn.setEffect(dropShadow);
        back_btn.setEffect(dropShadow);
        login_btn.setEffect(dropShadow);
        signup_btn.setEffect(dropShadow);

        isNightMode = !isNightMode;
    }

    @FXML
    public void forgetPassClicked() throws IOException {
        ((Stage) back_btn.getScene().getWindow()).close();
        Stage stage =  new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Account.class.getResource("FirstWindow.fxml"));
        FirstWindowController.setJob("forget pass");
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> s = new ArrayList<>();
        s.add("Whats your favorite app?");
        s.add("Who’s your favorite pet?");
        s.add("If you could vacation anywhere in the world, where would it be?");
        s.add("What is your favorite animal?");
        s.add("Who’s your favorite person in the whole world?");
        s.add("What is your favorite season?");
        q_combobox.getItems().addAll(s);
        q_combobox.getSelectionModel().selectFirst();
    }
}
