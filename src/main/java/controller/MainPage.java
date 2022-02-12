package controller;

import controller.utlis.JsonUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainPage {
    @FXML
    private AnchorPane titleBar;

    /**
     * 初始化stage
     */
    @FXML
    private void initialize() {

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

}
