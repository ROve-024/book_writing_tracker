package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import io.project.Project;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.JsonUtils;
import utils.ProjectUtils;
import utils.UserProjectUtils;
import utils.UserUtils;

import java.util.List;

public class MainPage {
    @FXML
    private AnchorPane titleBar;
    @FXML
    private ScrollPane projectScrollArea;


    /**
     * 初始化stage
     */
    @FXML
    private void initialize() {
        updateProjectListArea();

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
     * 更新project栏的内容
     */
    private void updateProjectListArea(){
        List<Project> projectList = UserProjectUtils.getProjectListByUserId(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/infoLogin.json").get("username")));
        AnchorPane projectArea = new AnchorPane();
        int i = 0;
        for(Project project : projectList){
            int y = 60 * i;
            // 添加一个项目容器
            AnchorPane projectItem = new AnchorPane(){
                {
                    setLayoutX(0);
                    setLayoutY(y);
                    setPrefWidth(280);
                    setPrefWidth(60);
                }
            };

            // 项目名
            Label projectName = new Label(project.getProjectName()){
                {
                    setLayoutX(14);
                    setLayoutY(5);
                }
            };
            projectName.setFont(Font.font ("Arial", 16));
            projectName.setStyle("-fx-text-fill: #232323");
            projectItem.getChildren().add(projectName);

            // 项目名
            Label projectId = new Label(project.getIdProject()){
                {
                    setLayoutX(0);
                    setLayoutY(0);
                }
            };
            projectId.setVisible(false);
            projectItem.getChildren().add(projectId);

            double taskSituation = ProjectUtils.getProjectProcessByIdProject(project.getIdProject());

            // 项目进度
            AnchorPane progressBarArea = new AnchorPane(){
                {
                    setLayoutX(14);
                    setLayoutY(35);
                    setPrefWidth(220);
                    setPrefHeight(10);
                }
            };
            progressBarArea.setStyle("-fx-background-radius: 50px; -fx-background-color:#eeeeee");

            AnchorPane progressBarContent = new AnchorPane(){
                {
                    setLayoutX(0);
                    setLayoutY(1);
                    setPrefWidth(Math.round(218 *taskSituation));
                    setPrefHeight(8);
                }
            };
            progressBarContent.setStyle("-fx-background-radius: 50px; -fx-background-color:#99aaaa");
            progressBarArea.getChildren().add(progressBarContent);
            projectItem.getChildren().add(progressBarArea);

            // 项目进度
            Label projectSituation = new Label(Math.round(100 *taskSituation) + "%"){
                {
                    setLayoutX(246);
                    setLayoutY(33);
                }
            };
            if(Math.round(100 *taskSituation) == 100){
                projectSituation.setLayoutX(239);
            }
            projectSituation.setFont(Font.font ("Arial", 12));
            projectSituation.setStyle("-fx-text-fill: #777777");
            projectSituation.setTextAlignment(TextAlignment.RIGHT);
            projectItem.getChildren().add(projectSituation);

            // 装饰线
            AnchorPane bottomDecorationLine = new AnchorPane(){
                {
                    setLayoutX(0);
                    setLayoutY(59);
                    setPrefWidth(280);
                    setPrefHeight(1);
                }
            };
            bottomDecorationLine.setStyle("-fx-background-radius: 1px; -fx-background-color:#e7e7e7");
            projectItem.getChildren().add(bottomDecorationLine);

            projectItem.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("Project id = " + projectId.getText());
                    updateTaskListArea(projectId.getText());
                }
            });

            projectArea.getChildren().add(projectItem);
            i++;
        }

        projectScrollArea.setContent(projectArea);
        projectScrollArea.setFitToWidth(true);
        projectScrollArea.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        projectScrollArea.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    /**
     * 更新人物列表栏的内容
     */
    private void updateTaskListArea(String idProject){

    }

}
