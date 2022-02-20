package ui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.WindowsUtils;
import utils.JsonUtils;
import utils.OtherUtils;
import utils.UserUtils;

import java.io.File;
import java.io.IOException;

/**
 * Login.fxml的控制器
 *
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class Login {
    JSONObject buffer;
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

    /**
     * 初始化stage
     */
    @FXML
    private void initialize() {
        wrongInputTips.setVisible(false);
        tooManyWrongInputTips.setVisible(false);
        buffer = JsonUtils.getBuffer();
        usernameInput.setText((String) buffer.get("username"));
        rememberMeCheckbox.setStyle("-jfx-checked-color: #99aaaa; -jfx-unchecked-color: #bbbbbb; -fx-text-fill: " +
                "#777777");
    }

    /**
     * 点击关闭按钮，结束程序运行
     */
    @FXML
    protected void closeButtonAction() {
        System.exit(0);
    }

    /**
     * 点击最小化按钮，最小化窗口
     */
    @FXML
    protected void minimizeButtonAction() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * 点击登录按钮，验证用户身份
     */
    @FXML
    protected void loginButtonAction() {
        String username = usernameInput.getText();
        String password = OtherUtils.encryptByMD5(passwordInput.getText());
        if (UserUtils.loginMatch(username, password)) {
            buffer.put("username", username);
            buffer.put("idUser", UserUtils.getIDByUsername(username));
            buffer.put("page", "homePage");
            if (rememberMeCheckbox.isSelected()) {
                buffer.put("userStatus", OtherUtils.encryptByMD5("true"));
            } else {
                buffer.put("userStatus", OtherUtils.encryptByMD5("false"));
            }
            JsonUtils.setBuffer(buffer);
            wrongInputTips.setVisible(false);
            Parent root = null;
            try {
                root = FXMLLoader.load(new File("src/main/java/ui/fxml/MainPage.fxml").toURI().toURL());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            WindowsUtils.initial(root, (Stage) signUpButton.getScene().getWindow());
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

    /**
     * 跳转到忘记密码页面
     */
    @FXML
    protected void forgetPasswordButtonAction() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/ui/fxml/ForgotPassword.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        WindowsUtils.initial(root, stage);
        forgetPasswordButton.getScene().setRoot(root);
    }


    /**
     * 跳转到注册用户页面
     */
    @FXML
    protected void signUpButtonAction() {
        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/ui/fxml/SignUp.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        WindowsUtils.initial(root, (Stage) signUpButton.getScene().getWindow());
        signUpButton.getScene().setRoot(root);
    }


}
