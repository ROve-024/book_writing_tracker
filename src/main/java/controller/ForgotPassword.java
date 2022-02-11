package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.utlis.InitialWindows;
import controller.utlis.UserUtils;
import controller.utlis.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.user.User;

import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;

public class ForgotPassword {
    private int mistake = 0;
    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXPasswordField repeatPasswordInput;
    @FXML
    private JFXTextField securityQuestion;
    @FXML
    private JFXTextField questionAnswerInput;
    @FXML
    private Label usernameWrongInputTips;
    @FXML
    private Label formatWrongInputTips;
    @FXML
    private Label repeatWrongInputTips;
    @FXML
    private Label answerWrongInputTips;
    @FXML
    private Label tooManyWrongInputTips;
    @FXML
    private JFXButton resetButton;

    User user;

    @FXML
    private void initialize() {
        usernameWrongInputTips.setVisible(false);
        formatWrongInputTips.setVisible(false);
        repeatWrongInputTips.setVisible(false);
        answerWrongInputTips.setVisible(false);
        securityQuestion.setDisable(true);
        securityQuestion.setText("Please enter correct username");
        tooManyWrongInputTips.setVisible(false);

        usernameInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                if (UserUtils.ifSameUsername(usernameInput.getText())) {
                    usernameWrongInputTips.setVisible(false);
                    user = UserUtils.searchUserByUsername(usernameInput.getText());
                    securityQuestion.setText(user.getQuestion());
                } else {
                    usernameWrongInputTips.setVisible(true);
                    securityQuestion.setText("Please enter correct username");
                }
            }
        });
        passwordInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1 && UserUtils.ifPasswordValid(passwordInput.getText())) {
                formatWrongInputTips.setVisible(true);
            }
        });
        repeatPasswordInput.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!passwordInput.getText().equals(repeatPasswordInput.getText())) {
                repeatWrongInputTips.setVisible(true);
            }
        });

    }

    @FXML
    protected void closeButtonAction() {
        System.exit(0);
    }

    @FXML
    protected void minimizeButtonAction() {
        Stage stage = (Stage) questionAnswerInput.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void resetButtonAcction() {
        boolean flag = true;
        if (!UserUtils.ifSameUsername(usernameInput.getText()) && !usernameInput.getText().isEmpty()) {
            flag = false;
            usernameWrongInputTips.setVisible(true);
            user = UserUtils.searchUserByUsername(usernameInput.getText());
        } else {
            usernameWrongInputTips.setVisible(false);
        }
        if (UserUtils.ifPasswordValid(passwordInput.getText())) {
            flag = false;
            formatWrongInputTips.setVisible(true);
        } else {
            formatWrongInputTips.setVisible(false);
        }
        if (!passwordInput.getText().equals(repeatPasswordInput.getText())) {
            flag = false;
            repeatWrongInputTips.setVisible(true);
        } else {
            repeatWrongInputTips.setVisible(false);
        }
        if (user == null || !securityQuestion.getText().equals(user.getQuestion())) {
            flag = false;
        }
        if (user == null || !Utils.encryptByMD5(questionAnswerInput.getText()).equals(user.getAnswer())) {
            flag = false;
            repeatWrongInputTips.setVisible(true);
        } else {
            repeatWrongInputTips.setVisible(false);
        }

        if (flag) {
            user.setPassword(Utils.encryptByMD5(passwordInput.getText()));
            UserUtils.updateUserList(user);
            System.out.println("success");
            Parent root = null;
            try {
                root = FXMLLoader.load(new File("src/main/java/view/Login.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            usernameInput.getScene().setRoot(root);
        } else {
            answerWrongInputTips.setVisible(true);
            tooManyWrongInputTips.setVisible(false);
            mistake++;
            if (mistake == 5) {
                answerWrongInputTips.setVisible(false);
                tooManyWrongInputTips.setVisible(true);
                mistake = 0;
                new Thread(() -> {
                    Platform.runLater(() -> resetButton.setDisable(true));
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {

                    }
                    Platform.runLater(() -> resetButton.setDisable(false));
                    tooManyWrongInputTips.setVisible(false);
                }).start();

            }
        }
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
        InitialWindows.initial(root, (Stage) securityQuestion.getScene().getWindow());
        securityQuestion.getScene().setRoot(root);
    }

}
