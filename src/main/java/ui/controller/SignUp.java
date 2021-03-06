package ui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.user.User;
import utils.JsonUtils;
import utils.WindowsUtils;
import utils.UserUtils;
import utils.OtherUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SignUp {
    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXPasswordField repeatPasswordInput;
    @FXML
    private JFXComboBox securityQuestionBox;
    @FXML
    private JFXTextField questionAnswerInput;
    @FXML
    private Label usernameWrongInputTips;
    @FXML
    private Label usernameEmptyInputTips;
    @FXML
    private Label formatWrongInputTips;
    @FXML
    private Label repeatWrongInputTips;
    @FXML
    private Label answerWrongInputTips;
    @FXML
    private Hyperlink signInButton;


    /**
     * 初始化stage
     */
    @FXML
    private void initialize() {
        usernameWrongInputTips.setVisible(false);
        usernameEmptyInputTips.setVisible(false);
        formatWrongInputTips.setVisible(false);
        repeatWrongInputTips.setVisible(false);
        answerWrongInputTips.setVisible(false);
        securityQuestionBox.getItems().removeAll(securityQuestionBox.getItems());
        securityQuestionBox.getItems().addAll("What city were you born in?", "What is your oldest sibling’s middle name?", "What was the first concert you attended?", "What was the make and model of your first car?", "In what city or town did your parents meet?");
        securityQuestionBox.getSelectionModel().select("What city were you born in?");
        usernameInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                if (UserUtils.isSameUsername(usernameInput.getText())) {
                    usernameWrongInputTips.setVisible(true);
                    usernameEmptyInputTips.setVisible(false);
                } else if (usernameInput.getText().isEmpty()) {
                    usernameWrongInputTips.setVisible(false);
                    usernameEmptyInputTips.setVisible(true);
                } else {
                    usernameWrongInputTips.setVisible(false);
                    usernameEmptyInputTips.setVisible(false);
                }

            }
        });
        passwordInput.textProperty().addListener((observableValue, oldValue, newValue) ->{
                formatWrongInputTips.setVisible(!Objects.equals(oldValue, newValue) && UserUtils.isPasswordValid(passwordInput.getText()));
        });
        repeatPasswordInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            repeatWrongInputTips.setVisible(!Objects.equals(oldValue, newValue) && !passwordInput.getText().equals(repeatPasswordInput.getText()));
        });
    }

    /**
     * 点击提交按钮，验证用户输入数据是否合法，合法时将数据保存，并跳转到登录页面
     */
    @FXML
    protected void loginButtonAction() {
        boolean flag = true;
        if (UserUtils.isSameUsername(usernameInput.getText())) {
            flag = false;
            usernameWrongInputTips.setVisible(true);
        } else {
            usernameWrongInputTips.setVisible(false);
        }
        if (usernameInput.getText().isEmpty()) {
            flag = false;
            usernameEmptyInputTips.setVisible(true);
        } else {
            usernameEmptyInputTips.setVisible(false);
        }
        if (UserUtils.isPasswordValid(passwordInput.getText())) {
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
        if (questionAnswerInput.getText().isEmpty()) {
            flag = false;
            answerWrongInputTips.setVisible(true);
        } else {
            answerWrongInputTips.setVisible(false);
        }

        if (flag) {
            UserUtils.signUpSubmit(usernameInput.getText(), OtherUtils.encryptByMD5(passwordInput.getText()), (String) securityQuestionBox.getValue(), OtherUtils.encryptByMD5(questionAnswerInput.getText()));
            User user = UserUtils.getUserByUsername(usernameInput.getText());
            JSONObject buffer = JsonUtils.getBuffer();
            buffer.put("username", user.getUsername());
            buffer.put("idUser", user.getIdUser());
            JsonUtils.setBuffer(buffer);
            Parent root = null;
            try {
                root = FXMLLoader.load(new File("src/main/java/ui/fxml/Login.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            usernameInput.getScene().setRoot(root);
        }

    }

    /**
     * 点击关闭按钮，结束程序运行
     */
    @FXML
    protected void closeButtonAction() {
        System.exit(0);
    }

    /**
     * 点击最小化按钮，将应用窗口最小化
     */
    @FXML
    protected void minimizeButtonAction() {
        Stage stage = (Stage) usernameInput.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * 跳转到登录页面
     */
    @FXML
    protected void signInButtonAction() {
        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/ui/fxml/Login.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        WindowsUtils.initial(root, (Stage) signInButton.getScene().getWindow());
        signInButton.getScene().setRoot(root);
    }

}
