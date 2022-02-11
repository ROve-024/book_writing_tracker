package controller;

import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.utlis.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Login {
    JSONObject user;
    private int mistake = 0;

    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXCheckBox rememberMeCheckbox;
    @FXML
    private Hyperlink forgetPasswordButton;
    @FXML
    private Hyperlink signUpButton;
    @FXML
    private JFXButton loginButton;
    @FXML
    private Label wrongInputTips;
    @FXML
    private Label tooManyWrongInputTips;
    @FXML
    private AnchorPane titleBar;

    @FXML
    private void initialize() {
        wrongInputTips.setVisible(false);
        tooManyWrongInputTips.setVisible(false);
        user = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/infoLogin.json");
        usernameInput.setText((String) user.get("username"));
    }

    @FXML
    protected void closeButtonAction() {
        System.exit(0);
    }

    @FXML
    protected void minimizeButtonAction() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void loginButtonAcction() {
        String username = usernameInput.getText();
        String password = Utils.encryptByMD5(passwordInput.getText());
        if (UserUtils.loginMatch(username, password)) {
            if (rememberMeCheckbox.isSelected()) {
                user.put("status", Utils.encryptByMD5("true"));
            } else {
                user.put("status", Utils.encryptByMD5("false"));
            }
            JsonUtils.saveJsonToFile(user, "src/main/resources/buffer/infoLogin.json");
            wrongInputTips.setVisible(false);
            Parent root = null;
            try {
                root = FXMLLoader.load(new File("src/main/java/view/MainPage.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            InitialWindows.initial(root, (Stage) signUpButton.getScene().getWindow());
            signUpButton.getScene().setRoot(root);
        } else {
            wrongInputTips.setVisible(true);
            tooManyWrongInputTips.setVisible(false);
            mistake++;
            if (mistake == 5) {
                wrongInputTips.setVisible(false);
                tooManyWrongInputTips.setVisible(true);
                mistake = 0;
                new Thread(() -> {
                    Platform.runLater(() -> loginButton.setDisable(true));
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {
                    }
                    Platform.runLater(() -> loginButton.setDisable(false));
                }).start();

            }
        }

    }

    @FXML
    protected void forgetPasswordButtonAction() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/view/ForgotPassword.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        InitialWindows.initial(root, stage);
        forgetPasswordButton.getScene().setRoot(root);
    }

    @FXML
    protected void signUpButtonAction() {
        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/view/SignUp.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        InitialWindows.initial(root, (Stage) signUpButton.getScene().getWindow());
        signUpButton.getScene().setRoot(root);
    }


}
