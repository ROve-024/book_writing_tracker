package ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import io.project.Project;
import io.userproject.UserProject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import utils.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class MainPage {
    private final Popup popup = new Popup();
    @FXML
    private AnchorPane titleBar;
    @FXML
    private ScrollPane projectScrollArea;
    @FXML
    private JFXButton deadlineButton;
    @FXML
    private JFXButton createTimeButton;
    @FXML
    private JFXButton scheduleButton;
    @FXML
    private JFXButton newProjectButton;


    /**
     * 初始化stage
     */
    @FXML
    private void initialize() {
        sortProjectByCreateTimeAction();
        initialPopupProjectEditor();

        newProjectButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (!popup.isShowing()){
                    popup.show(newProjectButton.getScene().getWindow());
                }
            }
        });


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
     * 点击根据deadline排序project
     */
    @FXML
    protected void sortProjectByDeadLineAction() {
        List<Project> projectList = UserProjectUtils.getProjectListByUserId(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/infoLogin.json").get("username")));
        ProjectUtils.sortTaskByDeadlineOrder(projectList);
        deadlineButton.setStyle("-fx-text-fill: #eeeeee; -fx-background-color: #9EA9B0; -fx-font-weight: bold");
        createTimeButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        scheduleButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");

        updateProjectListArea(projectList);
    }

    /**
     * 点击根据create time排序project
     */
    @FXML
    protected void sortProjectByCreateTimeAction() {
        List<Project> projectList = UserProjectUtils.getProjectListByUserId(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/infoLogin.json").get("username")));
        ProjectUtils.sortTaskByCreateTimeOrder(projectList);
        createTimeButton.setStyle("-fx-text-fill: #eeeeee; -fx-background-color: #9EA9B0; -fx-font-weight: bold");
        deadlineButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        scheduleButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");

        updateProjectListArea(projectList);
    }

    /**
     * 点击根据schedule排序project
     */
    @FXML
    protected void sortProjectByScheduleAction() {
        List<Project> projectList = UserProjectUtils.getProjectListByUserId(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/infoLogin.json").get("username")));
        ProjectUtils.sortTaskByScheduleOrder(projectList);
        scheduleButton.setStyle("-fx-text-fill: #eeeeee; -fx-background-color: #9EA9B0; -fx-font-weight: bold");
        createTimeButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        deadlineButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");

        updateProjectListArea(projectList);
    }

    /**
     * 点击新建一个project
     */
    @FXML
    protected void newProjectAction() {
        // TODO:..

//        updateProjectListArea(projectList);
    }

    /**
     * 点击编辑一个project
     */
    @FXML
    protected void editProjectAction() {
        // TODO:..

//        updateProjectListArea(projectList);
    }

    /**
     * 更新project栏的内容
     */
    private void updateProjectListArea(List<Project> projectList) {
        AnchorPane projectArea = new AnchorPane();
        int i = 0;
        for (Project project : projectList) {
            int y = 60 * i;
            // 添加一个项目容器
            AnchorPane projectItem = new AnchorPane() {
                {
                    setLayoutX(0);
                    setLayoutY(y);
                    setPrefWidth(280);
                    setPrefWidth(60);
                }
            };

            // 项目名
            Label projectName = new Label(project.getProjectName()) {
                {
                    setLayoutX(14);
                    setLayoutY(5);
                }
            };
            projectName.setFont(Font.font("Arial", 16));
            projectName.setStyle("-fx-text-fill: #232323");
            projectItem.getChildren().add(projectName);

            // 项目名
            Label projectId = new Label(project.getIdProject()) {
                {
                    setLayoutX(0);
                    setLayoutY(0);
                }
            };
            projectId.setVisible(false);
            projectItem.getChildren().add(projectId);

            double taskSituation = ProjectUtils.getProjectProcessByIdProject(project.getIdProject());

            // 项目进度
            AnchorPane progressBarArea = new AnchorPane() {
                {
                    setLayoutX(14);
                    setLayoutY(35);
                    setPrefWidth(220);
                    setPrefHeight(10);
                }
            };
            progressBarArea.setStyle("-fx-background-radius: 50px; -fx-background-color:#eeeeee");

            AnchorPane progressBarContent = new AnchorPane() {
                {
                    setLayoutX(0);
                    setLayoutY(1);
                    setPrefWidth(Math.round(218 * taskSituation));
                    setPrefHeight(8);
                }
            };
            progressBarContent.setStyle("-fx-background-radius: 50px; -fx-background-color:#99aaaa");
            progressBarArea.getChildren().add(progressBarContent);
            projectItem.getChildren().add(progressBarArea);

            // 项目进度
            Label projectSituation = new Label(Math.round(100 * taskSituation) + "%") {
                {
                    setLayoutX(246);
                    setLayoutY(33);
                }
            };
            if (Math.round(100 * taskSituation) == 100) {
                projectSituation.setLayoutX(239);
            }
            projectSituation.setFont(Font.font("Arial", 12));
            projectSituation.setStyle("-fx-text-fill: #777777");
            projectSituation.setTextAlignment(TextAlignment.RIGHT);
            projectItem.getChildren().add(projectSituation);

            // 装饰线
            AnchorPane bottomDecorationLine = new AnchorPane() {
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
    private void updateTaskListArea(String idProject) {

    }

    /**
     * 新建和编辑项目窗口
     */
    private void initialPopupProjectEditor(){
        popup.setAutoHide(true);
        AnchorPane projectEditorArea = new AnchorPane(){
            {
                setPrefWidth(420);
                setPrefHeight(335);
            }
        };
        projectEditorArea.getStylesheets().add("css/styles.css");
        projectEditorArea.getStylesheets().add("css/date_picker.css");
        projectEditorArea.setStyle("-fx-background-color: #f5f5f5");
        projectEditorArea.setEffect(new DropShadow(10, 0, 2, new Color(0.86,0.86,0.86, 1)));

        JFXTextField projectTitle = new JFXTextField(){
            {
                setLayoutX(15);
                setLayoutY(20);
                setPrefWidth(270);
                setPrefHeight(25);
            }
        };
        projectTitle.getStyleClass().add("text-field-without-line");
        projectTitle.setPromptText("Project title");
        projectEditorArea.getChildren().add(projectTitle);

        AnchorPane decorationLine1 = new AnchorPane(){
            {
                setLayoutX(15);
                setLayoutY(53);
                setPrefWidth(390);
                setPrefHeight(1);
            }
        };
        decorationLine1.setStyle("-fx-background-color: #dddddd");
        projectEditorArea.getChildren().add(decorationLine1);

        JFXTextArea projectDescription = new JFXTextArea(){
            {
                setLayoutX(15);
                setLayoutY(60);
                setPrefWidth(390);
                setPrefHeight(200);
            }
        };
        projectDescription.getStyleClass().add("text-field-without-line");
        projectDescription.setPromptText("Description of book writing project");
        projectEditorArea.getChildren().add(projectDescription);

        AnchorPane decorationLine2 = new AnchorPane(){
            {
                setLayoutX(15);
                setLayoutY(261);
                setPrefWidth(390);
                setPrefHeight(1);
            }
        };
        decorationLine2.setStyle("-fx-background-color: #dddddd");
        projectEditorArea.getChildren().add(decorationLine2);

        DatePicker datePicker = new DatePicker(){
            {
                setLayoutX(290);
                setLayoutY(22);
                setPrefWidth(115);
                setPrefHeight(25);
            }
        };
        WindowsUtils.datepickerInitial(datePicker);
        projectEditorArea.getChildren().add(datePicker);

        Label wrongProjectTitleTip = new Label("Project title cannot be empty."){
            {
                setLayoutX(15);
                setLayoutY(265);
            }
        };
        wrongProjectTitleTip.getStyleClass().add("wrong-tip");
        wrongProjectTitleTip.setVisible(false);
        projectEditorArea.getChildren().add(wrongProjectTitleTip);

        Label wrongProjectDeadlineTip = new Label("Project deadline cannot be empty."){
            {
                setLayoutX(15);
                setLayoutY(265);
            }
        };
        wrongProjectDeadlineTip.getStyleClass().add("wrong-tip");
        wrongProjectDeadlineTip.setVisible(false);
        projectEditorArea.getChildren().add(wrongProjectDeadlineTip);

        JFXButton saveButton = new JFXButton("SAVE"){
            {
                setLayoutX(15);
                setLayoutY(290);
                setPrefWidth(55);
                setPrefHeight(30);
            }
        };
        saveButton.setStyle("-fx-text-fill: #f5f5f5; -fx-background-color: #99aaaa; -fx-font-size: 12px; " +
                "-fx-font-weight: bold; -fx-background-radius: 5px");
        projectEditorArea.getChildren().add(saveButton);

        projectTitle.focusedProperty().addListener((observableValue, aBoolean, t1) ->{
            if (!t1) {
                if(projectTitle.getText().isEmpty()){
                    wrongProjectTitleTip.setVisible(true);
                    wrongProjectDeadlineTip.setVisible(false);
                } else {
                    wrongProjectTitleTip.setVisible(false);
                }

            }
        } );

        datePicker.focusedProperty().addListener((observableValue, aBoolean, t1) ->{
            if (!t1) {
                try {
                    wrongProjectDeadlineTip.setVisible(datePicker.getValue().equals(null));
                }catch (NullPointerException ignored){
                    wrongProjectDeadlineTip.setVisible(true);
                    wrongProjectTitleTip.setVisible(false);
                }

            }
        } );

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    datePicker.getValue().equals(null);
                    if(projectTitle.getText().isEmpty()){
                        wrongProjectTitleTip.setVisible(true);
                        wrongProjectDeadlineTip.setVisible(false);
                    } else {
                        wrongProjectTitleTip.setVisible(false);
                        Project project = new Project();
                        project.setIdProject(String.valueOf(ProjectUtils.maxProjectId(ProjectUtils.getProjectList())));
                        project.setMasterIdUser(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile(
                                "src/main/resources/buffer/infoLogin.json").get("username")));
                        project.setProjectName(projectTitle.getText());
                        project.setDescription(projectDescription.getText());
                        project.setCreateTime(new Date().getTime());
                        project.setDeadlineTime(OtherUtils.conventLocalDateToLong(datePicker.getValue()));
                        ProjectUtils.createProject(project);
                        sortProjectByCreateTimeAction();

                        UserProject userProject = new UserProject();
                        userProject.setIdProject(project.getIdProject());
                        userProject.setIdUser(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile(
                                "src/main/resources/buffer/infoLogin.json").get("username")));
                        UserProjectUtils.createUserProject(userProject);

                        popup.hide();
                    }
                }catch (NullPointerException ignored){
                    wrongProjectDeadlineTip.setVisible(true);
                    wrongProjectTitleTip.setVisible(false);
                }

            }
        });

        popup.getContent().add(projectEditorArea);

    }
}
