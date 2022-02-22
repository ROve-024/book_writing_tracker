package ui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import io.project.Project;
import io.task.Task;
import io.user.User;
import io.userproject.UserProject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainPage {
    private final Popup projectEditorPopUp = new Popup();
    private final Popup confirmDeletePopUp = new Popup();
    private final Popup selfProfilePopUp = new Popup();
    private final Popup selectExecutorPopUp = new Popup();
    private final Popup copyTipPopUp = new Popup();
    private final Popup joinProjectPopUp = new Popup();
    AnchorPane userListArea = new AnchorPane();
    @FXML
    private JFXButton homepageButton;
    @FXML
    private JFXButton myTaskButton;
    @FXML
    private JFXButton recycleBinButton;
    @FXML
    private JFXButton deadlineButton;
    @FXML
    private JFXButton createTimeButton;
    @FXML
    private JFXButton scheduleButton;
    @FXML
    private JFXButton newProjectButton;
    @FXML
    private JFXButton editProjectButton;
    @FXML
    private JFXButton deleteTaskButton;
    @FXML
    private JFXButton copyProjectCodeButton;
    @FXML
    private JFXButton joinProjectUsingCodeButton;
    @FXML
    private AnchorPane titleBar;
    @FXML
    private AnchorPane taskListUnselectedTip;
    @FXML
    private AnchorPane taskDetailArea;
    @FXML
    private AnchorPane subtaskArea;
    @FXML
    private AnchorPane userProfileButton;
    @FXML
    private ScrollPane projectScrollArea;
    @FXML
    private ScrollPane taskListScrollArea;
    @FXML
    private Label projectTitle;
    @FXML
    private Label projectUnselectedTextTip;
    @FXML
    private Label projectNoTaskTip;
    @FXML
    private Label wrongTaskTitleTip;
    @FXML
    private Label writerSelectButton;
    @FXML
    private Label proofreaderSelectButton;
    @FXML
    private Label taskCreatorUsername;
    @FXML
    private Label avatarLetter;
    @FXML
    private JFXTextField newTaskInput;
    @FXML
    private JFXTextField taskTitle;
    @FXML
    private JFXTextArea taskDescription;
    @FXML
    private JFXCheckBox writingTaskButton;
    @FXML
    private JFXCheckBox proofreadingTaskButton;
    @FXML
    private DatePicker taskDeadline;


    /**
     * 初始化stage
     */
    @FXML
    private void initialize() {
        JSONObject initialBuffer = JsonUtils.getBuffer();
        initialBuffer.put("projectEditorOption", "initial");
        initialBuffer.put("searchUserInput", "");
        JsonUtils.setBuffer(initialBuffer);
        sortProjectByCreateTimeAction();
        initialPopupProjectEditor();
        initialSelfProfilePopUp();
        initialSelectExecutorPopUp();
        initialCopyTipPopUp();
        initialJoinProjectPopUp();
        clearTaskListArea();
        clearTaskDetailArea();
        updateFirstLetter();
        newProjectButton.setOnMouseClicked(event -> {
            if (!projectEditorPopUp.isShowing()) {
                JSONObject projectEditorBuffer = JsonUtils.getBuffer();
                String projectEditorOption = (String) projectEditorBuffer.get("projectEditorOption");
                if (projectEditorOption.equals("edit")) {
                    projectEditorBuffer.put("projectEditorOption", "create");
                    JsonUtils.setBuffer(projectEditorBuffer);
                    initialPopupProjectEditor();
                }
                projectEditorPopUp.show(newProjectButton.getScene().getWindow());
            }
        });
        editProjectButton.setOnMouseClicked(event -> {
            if (!projectEditorPopUp.isShowing()) {
                JSONObject projectEditorBuffer = JsonUtils.getBuffer();
                projectEditorBuffer.put("projectEditorOption", "edit");
                JsonUtils.setBuffer(projectEditorBuffer);
                initialPopupProjectEditor();
                projectEditorPopUp.show(newProjectButton.getScene().getWindow());
            }
        });
        userProfileButton.setOnMouseClicked(event -> {
            if (!selfProfilePopUp.isShowing()) {
                selfProfilePopUp.show(projectTitle.getScene().getWindow());
            }
        });
        writerSelectButton.setOnMouseClicked(event -> {
            if (!selectExecutorPopUp.isShowing()) {
                JSONObject buffer = JsonUtils.getBuffer();
                buffer.put("typeSubtask", "writing task");
                JsonUtils.setBuffer(buffer);
                updateUserList(userListArea, buffer.getString("searchUserInput"));
                selectExecutorPopUp.show(projectTitle.getScene().getWindow());
            }
        });
        proofreaderSelectButton.setOnMouseClicked(event -> {
            if (!selectExecutorPopUp.isShowing()) {
                JSONObject buffer = JsonUtils.getBuffer();
                buffer.put("typeSubtask", "proofreading task");
                JsonUtils.setBuffer(buffer);
                updateUserList(userListArea, buffer.getString("searchUserInput"));
                selectExecutorPopUp.show(projectTitle.getScene().getWindow());
            }
        });
        copyProjectCodeButton.setOnMouseClicked(event -> {
            if (!copyTipPopUp.isShowing()) {
                final KeyFrame kf1 = new KeyFrame(Duration.millis(0), e -> {
                    copyTipPopUp.show(projectTitle.getScene().getWindow());
                });
                final KeyFrame kf2 = new KeyFrame(Duration.millis(1000), e -> {
                    copyTipPopUp.hide();
                });
                final Timeline timeline = new Timeline(kf1, kf2);
                Platform.runLater(timeline::play);
            }
        });
        joinProjectUsingCodeButton.setOnMouseClicked(event -> {
            if (!joinProjectPopUp.isShowing()) {
                joinProjectPopUp.show(projectTitle.getScene().getWindow());
            }
        });
        newTaskInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!newTaskInput.getText().isEmpty()) {
                    JSONObject buffer = JsonUtils.getBuffer();
                    String idProject = (String) buffer.get("idProject");
                    Task task = new Task();
                    task.setIdTask(String.valueOf(TaskUtils.getNextTaskId(TaskUtils.getTaskList())));
                    task.setIdUser(UserUtils.getIDByUsername((String) JsonUtils.getBuffer().get("username")));
                    task.setTaskTitle(newTaskInput.getText());
                    task.setIdProject(idProject);
                    TaskUtils.createTask(task);
                    updateTaskListArea();
                    if (buffer.get("sortMethod").toString().equals("createTime")) {
                        sortProjectByCreateTimeAction();
                    } else if (buffer.get("sortMethod").toString().equals("deadline")) {
                        sortProjectByDeadLineAction();
                    } else if (buffer.get("sortMethod").toString().equals("schedule")) {
                        sortProjectByScheduleAction();
                    }
                    newTaskInput.setText("");
                }
            }
        });
        taskTitle.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                if (taskTitle.getText().isEmpty()) {
                    wrongTaskTitleTip.setVisible(true);
                } else {
                    wrongTaskTitleTip.setVisible(false);
                    JSONObject buffer = JsonUtils.getBuffer();
                    Task task = TaskUtils.getTaskByIdTask(buffer.get("idTask").toString());
                    task.setTaskTitle(taskTitle.getText());
                    TaskUtils.updateTaskList(task);
                    updateTaskListArea();
                    updateTaskDetailArea();
                }

            }
        });
        taskDeadline.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1 && !(taskDeadline.getValue() == null)) {
                JSONObject buffer = JsonUtils.getBuffer();
                Task task = TaskUtils.getTaskByIdTask(buffer.get("idTask").toString());
                task.setDeadlineTime(OtherUtils.conventLocalDateToLong(taskDeadline.getValue()));
                TaskUtils.updateTaskList(task);
                updateTaskListArea();
                updateTaskDetailArea();
            }
        });
        taskDescription.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1 && !taskDescription.getText().isEmpty()) {
                JSONObject buffer = JsonUtils.getBuffer();
                Task task = TaskUtils.getTaskByIdTask(buffer.get("idTask").toString());
                task.setDescription(taskDescription.getText());
                TaskUtils.updateTaskList(task);
                updateTaskListArea();
                updateTaskDetailArea();
            }
        });
        WindowsUtils.toolBarInitial(homepageButton, myTaskButton, recycleBinButton);
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
        List<Project> projectList =
                UserProjectUtils.getProjectListByUserId(UserUtils.getIDByUsername((String) JsonUtils.getBuffer().get(
                        "username")));
        ProjectUtils.sortTaskByDeadlineOrder(projectList);
        deadlineButton.setStyle("-fx-text-fill: #eeeeee; -fx-background-color: #9EA9B0; -fx-font-weight: bold");
        createTimeButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        scheduleButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("sortMethod", "deadline");
        JsonUtils.setBuffer(buffer);
        updateProjectListArea(projectList);
    }

    /**
     * 点击根据create time排序project
     */
    @FXML
    protected void sortProjectByCreateTimeAction() {
        List<Project> projectList =
                UserProjectUtils.getProjectListByUserId(UserUtils.getIDByUsername((String) JsonUtils.getBuffer().get(
                        "username")));
        ProjectUtils.sortTaskByCreateTimeOrder(projectList);
        createTimeButton.setStyle("-fx-text-fill: #eeeeee; -fx-background-color: #9EA9B0; -fx-font-weight: bold");
        deadlineButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        scheduleButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("sortMethod", "createTime");
        JsonUtils.setBuffer(buffer);
        updateProjectListArea(projectList);
    }

    /**
     * 点击根据schedule排序project
     */
    @FXML
    protected void sortProjectByScheduleAction() {
        List<Project> projectList =
                UserProjectUtils.getProjectListByUserId(UserUtils.getIDByUsername((String) JsonUtils.getBuffer().get(
                        "username")));
        ProjectUtils.sortTaskByScheduleOrder(projectList);
        scheduleButton.setStyle("-fx-text-fill: #eeeeee; -fx-background-color: #9EA9B0; -fx-font-weight: bold");
        createTimeButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        deadlineButton.setStyle("-fx-text-fill: #777777; -fx-background-color: #eeeeee; -fx-font-weight: normal");
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("sortMethod", "schedule");
        JsonUtils.setBuffer(buffer);
        updateProjectListArea(projectList);
    }

    @FXML
    protected void homepageButtonAction() {
        JSONObject initialBuffer = JsonUtils.getBuffer();
        initialBuffer.put("projectEditorOption", "initial");
        JsonUtils.setBuffer(initialBuffer);
        sortProjectByCreateTimeAction();
        clearTaskListArea();
        clearTaskDetailArea();
    }

    @FXML
    protected void myTaskButtonAction() {
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("page", "myTask");
        JsonUtils.setBuffer(buffer);

        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/ui/fxml/MyTask.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        WindowsUtils.initial(root, (Stage) projectTitle.getScene().getWindow());
        projectTitle.getScene().setRoot(root);
    }

    @FXML
    protected void recycleBinButtonAction() {

    }

    @FXML
    protected void logoutButtonAction() {
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("userStatus", OtherUtils.encryptByMD5("false"));
        JsonUtils.setBuffer(buffer);
        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/ui/fxml/Login.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        WindowsUtils.initial(root, (Stage) projectTitle.getScene().getWindow());
        projectTitle.getScene().setRoot(root);
    }

    @FXML
    protected void deleteTaskButtonAction() {
        JSONObject buffer = JsonUtils.getBuffer();
        TaskUtils.deleteTaskByIdTask(buffer.get("idTask").toString());
        updateProjectListArea();
        updateTaskListArea();
        clearTaskDetailArea();
    }

    /**
     * 新建和编辑项目窗口
     */
    private void initialPopupProjectEditor() {
        projectEditorPopUp.setAutoHide(true);

        JSONObject buffer = JsonUtils.getBuffer();
        String projectEditorOption = (String) buffer.get("projectEditorOption");

        AnchorPane projectEditorArea = new AnchorPane() {
            {
                setPrefWidth(420);
                setPrefHeight(335);
            }
        };
        projectEditorArea.getStylesheets().add("css/styles.css");
        projectEditorArea.getStylesheets().add("css/date_picker.css");
        projectEditorArea.setStyle("-fx-background-color: #f5f5f5");
        projectEditorArea.setEffect(new DropShadow(10, 0, 2, new Color(0.8, 0.8, 0.8, 1)));

        JFXTextField projectTitle = new JFXTextField() {
            {
                setLayoutX(15);
                setLayoutY(20);
                setPrefWidth(270);
                setPrefHeight(25);
            }
        };
        projectTitle.getStyleClass().add("text-field-without-line");
        projectTitle.setPromptText("Project name");
        projectEditorArea.getChildren().add(projectTitle);

        AnchorPane decorationLine1 = new AnchorPane() {
            {
                setLayoutX(15);
                setLayoutY(53);
                setPrefWidth(390);
                setPrefHeight(1);
            }
        };
        decorationLine1.setStyle("-fx-background-color: #dddddd");
        projectEditorArea.getChildren().add(decorationLine1);

        JFXTextArea projectDescription = new JFXTextArea() {
            {
                setLayoutX(15);
                setLayoutY(60);
                setPrefWidth(390);
                setPrefHeight(200);
            }
        };
        projectDescription.getStyleClass().add("text-field-without-line");
        projectDescription.setStyle("-fx-text-fill: #999999!important; -fx-font-size: 14px");
        projectDescription.setPromptText("Description of book writing project");
        projectEditorArea.getChildren().add(projectDescription);

        AnchorPane decorationLine2 = new AnchorPane() {
            {
                setLayoutX(15);
                setLayoutY(261);
                setPrefWidth(390);
                setPrefHeight(1);
            }
        };
        decorationLine2.setStyle("-fx-background-color: #dddddd");
        projectEditorArea.getChildren().add(decorationLine2);

        DatePicker datePicker = new DatePicker() {
            {
                setLayoutX(290);
                setLayoutY(22);
                setPrefWidth(115);
                setPrefHeight(25);
            }
        };
        WindowsUtils.datepickerInitial(datePicker);
        projectEditorArea.getChildren().add(datePicker);

        Label wrongProjectTitleTip = new Label("Project name cannot be empty.") {
            {
                setLayoutX(15);
                setLayoutY(265);
            }
        };
        wrongProjectTitleTip.getStyleClass().add("wrong-tip");
        wrongProjectTitleTip.setVisible(false);
        projectEditorArea.getChildren().add(wrongProjectTitleTip);

        Label wrongProjectDeadlineTip = new Label("Project deadline cannot be empty.") {
            {
                setLayoutX(15);
                setLayoutY(265);
            }
        };
        wrongProjectDeadlineTip.getStyleClass().add("wrong-tip");
        wrongProjectDeadlineTip.setVisible(false);
        projectEditorArea.getChildren().add(wrongProjectDeadlineTip);

        JFXButton saveButton = new JFXButton("SAVE") {
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

        JFXButton deleteButton = new JFXButton("DELETE") {
            {
                setLayoutX(81);
                setLayoutY(290);
                setPrefWidth(70);
                setPrefHeight(30);
            }
        };
        deleteButton.setStyle("-fx-text-fill: #f5f5f5; -fx-background-color: #C8DEDE; -fx-font-size: 12px; " +
                "-fx-font-weight: bold; -fx-background-radius: 5px");
        projectEditorArea.getChildren().add(deleteButton);

        projectTitle.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                if (projectTitle.getText().isEmpty()) {
                    wrongProjectTitleTip.setVisible(true);
                    wrongProjectDeadlineTip.setVisible(false);
                } else {
                    wrongProjectTitleTip.setVisible(false);
                }

            }
        });

        datePicker.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                try {
                    wrongProjectDeadlineTip.setVisible(datePicker.getValue().equals(null));
                } catch (NullPointerException ignored) {
                    wrongProjectDeadlineTip.setVisible(true);
                    wrongProjectTitleTip.setVisible(false);
                }

            }
        });

        if (projectEditorOption.equals("edit") && !buffer.get("projectEditorOption").toString().equals("initial")) {
            String projectId = (String) buffer.get("idProject");
            Project project = ProjectUtils.getProjectByIdProject(projectId);
            projectTitle.setText(project.getProjectName());
            projectDescription.setText(project.getDescription());
            LocalDate date = OtherUtils.conventLongToLocalDate(project.getDeadlineTime());
            datePicker.setValue(date);
            String idUser = UserUtils.getIDByUsername((String) JsonUtils.getBuffer().get("username"));
            if (!idUser.equals(project.getMasterIdUser())) {
                Label onlyReadTip = new Label("Only the project manager can edit") {
                    {
                        setLayoutX(15);
                        setLayoutY(265);
                    }
                };
                onlyReadTip.getStyleClass().add("wrong-tip");
                projectEditorArea.getChildren().add(onlyReadTip);

                projectTitle.setDisable(true);
                projectDescription.setDisable(true);
                datePicker.setDisable(true);
                saveButton.setDisable(true);
                deleteButton.setVisible(false);
            } else {
                projectTitle.setDisable(false);
                projectDescription.setDisable(false);
                datePicker.setDisable(false);
                saveButton.setDisable(false);
                deleteButton.setVisible(true);
            }

        } else if (!projectEditorOption.equals("edit")) {
            deleteButton.setVisible(false);
        }

        saveButton.setOnAction(event -> {
            if (datePicker.getValue() == null) {
                wrongProjectDeadlineTip.setVisible(true);
                wrongProjectTitleTip.setVisible(false);
            } else if (projectTitle.getText().isEmpty()) {
                wrongProjectTitleTip.setVisible(true);
                wrongProjectDeadlineTip.setVisible(false);
            } else {
                wrongProjectTitleTip.setVisible(false);
                Project project = new Project();
                project.setIdProject(String.valueOf(ProjectUtils.getNextNewProjectId(ProjectUtils.getProjectList())));
                project.setMasterIdUser(UserUtils.getIDByUsername((String) JsonUtils.getBuffer().get("username")));
                project.setProjectName(projectTitle.getText());
                project.setDescription(projectDescription.getText());
                project.setDeadlineTime(OtherUtils.conventLocalDateToLong(datePicker.getValue()));
                ProjectUtils.updateProjectList(project);

                UserProject userProject = new UserProject();
                userProject.setIdProject(project.getIdProject());
                userProject.setIdUser(UserUtils.getIDByUsername((String) JsonUtils.getBuffer().get("username")));
                UserProjectUtils.createUserProject(userProject);

                projectTitle.setText("");
                projectDescription.setText("");
                datePicker.setValue(null);
                projectEditorPopUp.hide();

                updateProjectListArea();
            }
        });

        deleteButton.setOnAction(event -> {
            if (!confirmDeletePopUp.isShowing()) {
                initialConfirmDeletePopUp();
                confirmDeletePopUp.show(homepageButton.getScene().getWindow());
                projectEditorPopUp.hide();
            }
        });

        projectEditorPopUp.getContent().add(projectEditorArea);

    }

    /**
     * 确定删除项目界面
     */
    private void initialConfirmDeletePopUp() {
        confirmDeletePopUp.setAutoHide(true);
        AnchorPane deleteProjectArea = new AnchorPane() {
            {
                setPrefWidth(420);
                setPrefHeight(120);
            }
        };
        deleteProjectArea.getStylesheets().add("css/styles.css");
        deleteProjectArea.setStyle("-fx-background-color: #f5f5f5");
        deleteProjectArea.setEffect(new DropShadow(10, 0, 2, new Color(0.8, 0.8, 0.8, 1)));

        Project project = ProjectUtils.getProjectByIdProject((String) JsonUtils.getBuffer().get("idProject"));
        Label deleteProjectTitle = new Label("Please type project name to confirm") {
            {
                setLayoutX(13);
                setLayoutY(10);
                setPrefWidth(394);
                setPrefHeight(25);
            }
        };
        deleteProjectTitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777");
        deleteProjectArea.getChildren().add(deleteProjectTitle);

        JFXTextField confirmDeleteProjectInput = new JFXTextField() {
            {
                setLayoutX(13);
                setLayoutY(40);
                setPrefWidth(394);
                setPrefHeight(30);
            }
        };
        confirmDeleteProjectInput.getStyleClass().add("text-field-without-line-background");
        confirmDeleteProjectInput.setPromptText(project.getProjectName());
        deleteProjectArea.getChildren().add(confirmDeleteProjectInput);

        JFXButton confirmDeleteProjectButton = new JFXButton("I understand the consequences, delete this " +
                "project") {
            {
                setLayoutX(13);
                setLayoutY(80);
                setPrefWidth(394);
                setPrefHeight(30);
            }
        };
        confirmDeleteProjectButton.setStyle("-fx-text-fill: #f5f5f5; -fx-background-color: #99aaaa; -fx-font-size: 12px; " +
                "-fx-font-weight: bold; -fx-background-radius: 5px");
        deleteProjectArea.getChildren().add(confirmDeleteProjectButton);
        confirmDeleteProjectButton.setDisable(true);

        confirmDeleteProjectInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                if (confirmDeleteProjectInput.getText().equals(project.getProjectName())) {
                    confirmDeleteProjectButton.setDisable(false);
                }
            }
        });

        confirmDeleteProjectButton.setOnAction(event -> {
            ProjectUtils.deleteProjectByIdProject(project.getIdProject());
            updateProjectListArea();
            clearTaskListArea();
            clearTaskDetailArea();
            confirmDeletePopUp.hide();
        });

        confirmDeletePopUp.getContent().add(deleteProjectArea);
    }

    /**
     * 初始化用户自己的个人资料页，可编辑
     */
    private void initialSelfProfilePopUp() {
        selfProfilePopUp.setAutoHide(true);

        AtomicBoolean isUsernameInputValid = new AtomicBoolean(true);
        AtomicBoolean isEmailInputValid = new AtomicBoolean(true);
        AtomicBoolean isTelephoneInputValid = new AtomicBoolean(true);

        JSONObject buffer = JsonUtils.getBuffer();

        AnchorPane profileArea = new AnchorPane() {
            {
                setPrefWidth(280);
                setPrefHeight(400);
            }
        };
        profileArea.getStylesheets().add("css/styles.css");
        profileArea.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5px");
        profileArea.setEffect(new DropShadow(10, 0, 2, new Color(0.8, 0.8, 0.8, 1)));

        AnchorPane avatar = new AnchorPane() {
            {
                setLayoutX(90);
                setLayoutY(40);
                setPrefWidth(100);
                setPrefHeight(100);
            }
        };
        avatar.getStyleClass().add("avatar");


        JFXTextField usernameInput = new JFXTextField() {
            {
                setLayoutX(0);
                setLayoutY(152);
                setPrefWidth(280);
                setPrefHeight(25);
            }
        };
        usernameInput.getStyleClass().add("tx-no-line-center-title");

        AnchorPane decorationLine1 = new AnchorPane() {
            {
                setLayoutX(40);
                setLayoutY(200);
                setPrefWidth(200);
                setPrefHeight(1);
            }
        };
        decorationLine1.setStyle("-fx-background-color: #dddddd");

        JFXTextField emailInput = new JFXTextField() {
            {
                setLayoutX(0);
                setLayoutY(215);
                setPrefWidth(280);
                setPrefHeight(20);
            }
        };
        emailInput.getStyleClass().add("tx-no-line-center");
        emailInput.setPromptText("Enter email address");

        JFXTextField telephoneInput = new JFXTextField() {
            {
                setLayoutX(0);
                setLayoutY(245);
                setPrefWidth(280);
                setPrefHeight(20);
            }
        };
        telephoneInput.getStyleClass().add("tx-no-line-center");
        telephoneInput.setPromptText("Enter telephone number");

        Label wrongInputTip = new Label() {
            {
                setLayoutX(0);
                setLayoutY(345);
                setPrefWidth(280);
                setPrefHeight(20);
            }
        };
        wrongInputTip.getStyleClass().add("wrong-tip");
        wrongInputTip.setStyle("-fx-text-alignment: center; -fx-alignment: center");

        JFXButton savaButton = new JFXButton("- SAVE -") {
            {
                setLayoutX(0);
                setLayoutY(370);
                setPrefWidth(280);
                setPrefHeight(40);
            }
        };
        savaButton.setStyle("-fx-background-color: #99aaaa; -fx-text-fill: #f5f5f5; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-background-radius: 0px 0px 5px 5px");

        usernameInput.setText(buffer.get("username").toString());
        String email = UserUtils.getUserByID(buffer.get("idUser").toString()).getEmail();
        String telephone = UserUtils.getUserByID(buffer.get("idUser").toString()).getTelephone();
        if (!email.equals("NULL")) {
            emailInput.setText(email);
        }
        if (!telephone.equals("NULL")) {
            telephoneInput.setText(telephone);
        }

        usernameInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                if (usernameInput.getText().isEmpty()) {
                    isUsernameInputValid.set(false);
                } else if (!buffer.get("username").toString().equals(newValue) && UserUtils.isSameUsername(newValue)) {
                    isUsernameInputValid.set(false);
                }
                changeProfileCheck(isUsernameInputValid, isEmailInputValid, isTelephoneInputValid, wrongInputTip, savaButton);
            }
        });
        emailInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                if (!emailInput.getText().isEmpty()) {
                    isEmailInputValid.set(UserUtils.isValidEmailAddress(newValue));
                }
                changeProfileCheck(isUsernameInputValid, isEmailInputValid, isTelephoneInputValid, wrongInputTip, savaButton);
            }
        });
        telephoneInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                String telephoneNew = telephoneInput.getText();
                if (telephoneNew.length() > 11 || telephoneNew.length() < 5) {
                    isTelephoneInputValid.set(false);
                } else {
                    try {
                        Long.parseLong(telephoneNew);
                        isTelephoneInputValid.set(true);
                    } catch (Exception ignored) {
                        isTelephoneInputValid.set(false);
                    }
                }
                changeProfileCheck(isUsernameInputValid, isEmailInputValid, isTelephoneInputValid, wrongInputTip, savaButton);
            }
        });

        savaButton.setOnMouseClicked(event -> {
            if (isUsernameInputValid.get() && isEmailInputValid.get() && isTelephoneInputValid.get()) {
                User user = UserUtils.getUserByUsername(buffer.get("username").toString());
                user.setUsername(usernameInput.getText());
                user.setEmail(emailInput.getText());
                user.setTelephone(telephoneInput.getText());
                UserUtils.updateUserList(user);
                buffer.put("username", usernameInput.getText());
                JsonUtils.setBuffer(buffer);
                updateFirstLetter();
                selfProfilePopUp.hide();
            }
        });

        profileArea.getChildren().add(avatar);
        profileArea.getChildren().add(decorationLine1);
        profileArea.getChildren().add(usernameInput);
        profileArea.getChildren().add(emailInput);
        profileArea.getChildren().add(telephoneInput);
        profileArea.getChildren().add(wrongInputTip);
        profileArea.getChildren().add(savaButton);
        selfProfilePopUp.getContent().add(profileArea);
    }

    /**
     * 选择子任务执行人
     */
    private void initialSelectExecutorPopUp() {
        selectExecutorPopUp.setAutoHide(true);
        AnchorPane selectUserArea = new AnchorPane() {
            {
                setPrefWidth(250);
                setPrefHeight(350);
            }
        };
        selectUserArea.getStylesheets().add("css/styles.css");
        selectUserArea.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5px");
        selectUserArea.setEffect(new DropShadow(10, 0, 2, new Color(0.8, 0.8, 0.8, 1)));

        JFXTextField searchUserInput = new JFXTextField() {
            {
                setLayoutX(0);
                setLayoutY(0);
                setPrefWidth(250);
                setPrefHeight(30);
            }
        };
        searchUserInput.getStyleClass().add("text-field-without-line-background");
        searchUserInput.setStyle("-fx-padding: 11px, 0; -fx-text-fill: #777777; -fx-background-color: #f5f5f5; " +
                "-fx-font-size: 16px");
        searchUserInput.setPromptText("Search for user by username");

        AnchorPane decorationLine = new AnchorPane() {
            {
                setLayoutX(0);
                setLayoutY(40);
                setPrefWidth(250);
                setPrefHeight(1);
            }
        };
        decorationLine.setStyle("-fx-background-color: #dddddd");

        ScrollPane userListScrollPane = new ScrollPane() {
            {
                setLayoutX(0);
                setLayoutY(41);
                setPrefWidth(250);
                setPrefHeight(275);
            }
        };

        userListArea.setStyle("-fx-background-color: #f5f5f5");
        updateUserList(userListArea, searchUserInput.getText());

        searchUserInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                JSONObject buffer = JsonUtils.getBuffer();
                buffer.put("searchUserInput", searchUserInput.getText());
                JsonUtils.setBuffer(buffer);
                updateUserList(userListArea, searchUserInput.getText());
            }
        });


        userListScrollPane.setContent(userListArea);
        userListScrollPane.setFitToWidth(true);
        userListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        userListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        selectUserArea.getChildren().add(searchUserInput);
        selectUserArea.getChildren().add(decorationLine);
        selectUserArea.getChildren().add(userListScrollPane);
        selectExecutorPopUp.getContent().add(selectUserArea);
    }

    /**
     * 初始化复制到剪贴板提示
     */
    private void initialCopyTipPopUp() {
        copyTipPopUp.setAutoHide(true);
        String projectCode = OtherUtils.encryptByMD5(JsonUtils.getBuffer().getString("idProject"));
        final ClipboardContent content = new ClipboardContent();
        content.putString(projectCode);
        Clipboard.getSystemClipboard().setContent(content);

        AnchorPane container = new AnchorPane() {
            {
                setPrefHeight(40);
                setPrefWidth(280);
            }
        };
        container.getStylesheets().add("css/styles.css");
        container.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5px");
        container.setEffect(new DropShadow(10, 0, 2, new Color(0.9, 0.9, 0.9, 1)));

        Label tip = new Label("Copied invitation code to clipboard.") {
            {
                setLayoutX(15);
                setLayoutY(10);
                setPrefHeight(20);
                setPrefWidth(250);
            }
        };
        tip.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777; -fx-alignment: center; -fx-text-alignment: center");
        container.getChildren().add(tip);

        copyTipPopUp.getContent().add(container);
    }

    /**
     * 初始化使用邀请码加入项目界面
     */
    private void initialJoinProjectPopUp() {
        joinProjectPopUp.setAutoHide(true);

        JSONObject buffer = JsonUtils.getBuffer();

        final String[] idProject = new String[1];

        AnchorPane container = new AnchorPane() {
            {
                setPrefHeight(110);
                setPrefWidth(280);
            }
        };
        container.getStylesheets().add("css/styles.css");
        container.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5px");
        container.setEffect(new DropShadow(10, 0, 2, new Color(0.8, 0.8, 0.8, 1)));

        Label tip = new Label("Enter invitation code") {
            {
                setLayoutX(10);
                setLayoutY(12);
            }
        };
        tip.setStyle("-fx-text-fill: #232323; -fx-font-size: 14px; -fx-font-weight: bold");

        JFXTextField invitationCodeInput = new JFXTextField() {
            {
                setLayoutX(10);
                setLayoutY(39);
                setPrefHeight(30);
                setPrefWidth(260);
            }
        };
        invitationCodeInput.getStyleClass().add("text-field-without-line-background");
        invitationCodeInput.setStyle("-fx-font-size: 11px; -fx-text-alignment: center; -fx-alignment: " +
                "center");
        invitationCodeInput.setPromptText("XXXXXXXX XXXXXXXX XXXXXXXX XXXXXXXX");

        Label wrongTip = new Label() {
            {
                setLayoutX(10);
                setLayoutY(75);
                setPrefWidth(260);
            }
        };
        wrongTip.getStyleClass().add("wrong-tip");
        wrongTip.setVisible(false);

        JFXButton submitButton = new JFXButton("JOIN") {
            {
                setLayoutX(0);
                setLayoutY(80);
                setPrefHeight(30);
                setPrefWidth(280);
            }
        };
        submitButton.setStyle("-fx-background-color: #99aaaa; -fx-text-fill: #f5f5f5; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-background-radius: 0px 0px 5px 5px");
        submitButton.setDisable(true);

        invitationCodeInput.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                if (invitationCodeInput.getText().isEmpty()) {
                    submitButton.setDisable(true);
                    wrongTip.setVisible(false);
                    container.setPrefHeight(110);
                    submitButton.setLayoutY(80);
                } else if (ProjectUtils.getIdProjectByCode(invitationCodeInput.getText()).equals("-1")) {
                    submitButton.setDisable(true);
                    wrongTip.setVisible(true);
                    wrongTip.setText("Invalid invitation code");
                    container.setPrefHeight(130);
                    submitButton.setLayoutY(100);
                } else if (ProjectUtils.getIdProjectByCode(invitationCodeInput.getText()).equals("-2")) {
                    submitButton.setDisable(true);
                    wrongTip.setVisible(true);
                    wrongTip.setText("You have already joined the project");
                    container.setPrefHeight(130);
                    submitButton.setLayoutY(100);
                } else {
                    submitButton.setDisable(false);
                    wrongTip.setVisible(false);
                    container.setPrefHeight(110);
                    submitButton.setLayoutY(80);
                    idProject[0] = ProjectUtils.getIdProjectByCode(invitationCodeInput.getText());
                }
            }
        });

        submitButton.setOnMouseClicked(event -> {
            UserProjectUtils.createUserProject(new UserProject(buffer.getString("idUser"), idProject[0]));
            buffer.put("idProject", idProject[0]);
            JsonUtils.setBuffer(buffer);
            updateProjectListArea();
            updateTaskListArea();
            clearTaskDetailArea();
            joinProjectPopUp.hide();
        });

        container.getChildren().add(tip);
        container.getChildren().add(invitationCodeInput);
        container.getChildren().add(wrongTip);
        container.getChildren().add(submitButton);

        joinProjectPopUp.getContent().add(container);
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
                    setPrefHeight(60);
                }
            };
            projectItem.getStyleClass().add("item-container");

            // 项目名
            Label projectName = new Label(project.getProjectName()) {
                {
                    setLayoutX(14);
                    setLayoutY(8);
                }
            };
            projectName.setFont(Font.font("Arial", 14));
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
            } else if (Math.round(100 * taskSituation) < 10) {
                projectSituation.setLayoutX(253);
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

            projectItem.setOnMouseClicked(mouseEvent -> {
                JSONObject buffer = JsonUtils.getBuffer();
                buffer.put("idProject", projectId.getText());
                JsonUtils.setBuffer(buffer);
                clearTaskDetailArea();
                updateTaskListArea();
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
     * 更新project栏的内容
     */
    private void updateProjectListArea() {
        JSONObject buffer = JsonUtils.getBuffer();
        if (buffer.get("sortMethod").toString().equals("createTime")) {
            sortProjectByCreateTimeAction();
        } else if (buffer.get("sortMethod").toString().equals("deadline")) {
            sortProjectByDeadLineAction();
        } else if (buffer.get("sortMethod").toString().equals("schedule")) {
            sortProjectByScheduleAction();
        }
    }

    /**
     * 更新任务列表栏的内容
     */
    private void updateTaskListArea() {
        JSONObject buffer = JsonUtils.getBuffer();
        String idProject = buffer.get("idProject").toString();
        clearTaskDetailArea();

        User user = UserUtils.getUserByID(UserUtils.getIDByUsername((String) buffer.get("username")));

        projectTitle.setText(ProjectUtils.getProjectByIdProject(idProject).getProjectName());
        List<Task> finishedTaskList = TaskUtils.getFinishedTaskListByIdProject(idProject);
        List<Task> unfinishedTaskList = TaskUtils.getUnfinishedTaskListByIdProject(idProject);

        projectTitle.setDisable(false);
        editProjectButton.setDisable(false);
        copyProjectCodeButton.setDisable(false);
        newTaskInput.setDisable(false);
        taskListScrollArea.setVisible(true);
        if (finishedTaskList.isEmpty() && unfinishedTaskList.isEmpty()) {
            taskListUnselectedTip.setVisible(true);
            projectNoTaskTip.setVisible(true);
            projectUnselectedTextTip.setVisible(false);
            taskListScrollArea.setVisible(false);
        } else {
            // 非空时，加载任务列表
            taskListUnselectedTip.setVisible(false);
            int nextPaneY = 0;

            AnchorPane taskListArea = new AnchorPane();
            if (!unfinishedTaskList.isEmpty()) {
                int tempY = nextPaneY;
                AnchorPane ongoingArea = new AnchorPane() {
                    {
                        setLayoutX(0);
                        setLayoutY(0);
                        setPrefWidth(405);
                        setPrefHeight(30);
                    }
                };
                ongoingArea.setStyle("-fx-background-color: #f1f1f1");
                nextPaneY += 28;

                Label ongoingTips = new Label("ONGOING") {
                    {
                        setLayoutX(14);
                        setLayoutY(6);
                    }
                };
                ongoingTips.setStyle("-fx-text-fill: #777777; -fx-font-size: 12px");
                ongoingArea.getChildren().add(ongoingTips);
                taskListArea.getChildren().add(ongoingArea);

                for (Task task : unfinishedTaskList) {
                    int tempy = nextPaneY;
                    AnchorPane taskItem = new AnchorPane() {
                        {
                            setLayoutX(0);
                            setLayoutY(tempy);
                            setPrefWidth(405);
                            setPrefHeight(40);
                        }
                    };
                    taskItem.getStyleClass().add("item-container");
                    nextPaneY += 40;

                    JFXCheckBox taskCheckBox = new JFXCheckBox() {
                        {
                            setLayoutX(14);
                            setLayoutY(12);
                            setPrefWidth(25);
                        }
                    };

                    taskCheckBox.setStyle("-fx-font-size: 14px; -jfx-checked-color: #99aaaa; -jfx-unchecked-color: " +
                            "#bbbbbb; -fx-text-fill: #232323");

                    Label taskName = new Label(task.getTaskTitle()) {
                        {
                            setLayoutX(36);
                            setLayoutY(12);
                            setPrefWidth(280);
                        }
                    };

                    if (!TaskUtils.isTaskAndSubtaskContainUser(task)) {
                        taskCheckBox.setDisable(true);
                        taskName.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777");
                    } else {
                        taskName.setStyle("-fx-font-size: 14px; -fx-text-fill: #232323");
                    }
                    taskItem.getChildren().add(taskCheckBox);
                    taskItem.getChildren().add(taskName);

                    taskCheckBox.setOnMouseClicked(mouseEvent -> {
                        JSONObject newBuffer = JsonUtils.getBuffer();
                        newBuffer.put("idTask", task.getIdTask());
                        JsonUtils.setBuffer(newBuffer);
                        task.setStatus("finished");
                        TaskUtils.updateTaskList(task);
                        updateProjectListArea();
                        updateTaskListArea();
                        updateTaskDetailArea();
                    });
                    taskName.setOnMouseClicked(mouseEvent -> {
                        JSONObject newBuffer = JsonUtils.getBuffer();
                        newBuffer.put("idTask", task.getIdTask());
                        JsonUtils.setBuffer(newBuffer);
                        updateTaskDetailArea();
                    });
                    taskItem.setOnMouseClicked(mouseEvent -> {
                        JSONObject newBuffer = JsonUtils.getBuffer();
                        newBuffer.put("idTask", task.getIdTask());
                        JsonUtils.setBuffer(newBuffer);
                        updateTaskDetailArea();
                    });

                    // 设置任务截止日期
                    long timestamp = task.getDeadlineTime();
                    if (timestamp != -1) {
                        Label taskDeadline =
                                new Label(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
                                        .toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) {
                                    {
                                        setLayoutX(315);
                                        setLayoutY(13);
                                    }
                                };
                        taskDeadline.setStyle("-fx-text-fill: #bbbbbb; -fx-font-size: 14px; -fx-font-style: italic");
                        taskItem.getChildren().add(taskDeadline);
                    }

                    AnchorPane decorationLine = new AnchorPane() {
                        {
                            setLayoutX(0);
                            setLayoutY(40);
                            setPrefWidth(405);
                            setPrefHeight(1);
                        }
                    };
                    decorationLine.setStyle("-fx-background-color: #eeeeee");
                    taskItem.getChildren().add(decorationLine);

                    taskListArea.getChildren().add(taskItem);
                }
            }

            if (!finishedTaskList.isEmpty()) {
                int tempY = nextPaneY;
                AnchorPane ongoingArea = new AnchorPane() {
                    {
                        setLayoutX(0);
                        setLayoutY(tempY);
                        setPrefWidth(405);
                        setPrefHeight(30);
                    }
                };
                ongoingArea.setStyle("-fx-background-color: #f1f1f1");
                nextPaneY += 38;

                Label ongoingTips = new Label("FINISHED") {
                    {
                        setLayoutX(14);
                        setLayoutY(6);
                    }
                };
                ongoingTips.setStyle("-fx-text-fill: #777777; -fx-font-size: 12px");
                ongoingArea.getChildren().add(ongoingTips);
                taskListArea.getChildren().add(ongoingArea);

                nextPaneY -= 10;

                for (Task task : finishedTaskList) {
                    int tempy = nextPaneY;
                    AnchorPane taskItem = new AnchorPane() {
                        {
                            setLayoutX(0);
                            setLayoutY(tempy);
                            setPrefWidth(405);
                            setPrefHeight(40);
                        }
                    };
                    taskItem.getStyleClass().add("item-container");
                    nextPaneY += 40;

                    JFXCheckBox taskCheckBox = new JFXCheckBox() {
                        {
                            setLayoutX(14);
                            setLayoutY(12);
                            setPrefWidth(25);
                        }
                    };
                    taskCheckBox.setSelected(true);
                    taskCheckBox.setStyle("-fx-font-size: 14px; -jfx-checked-color: #99aaaa; -jfx-unchecked-color: " +
                            "#bbbbbb; -fx-text-fill: #232323");
                    taskCheckBox.setText("");

                    Label taskName = new Label(task.getTaskTitle()) {
                        {
                            setLayoutX(36);
                            setLayoutY(12);
                            setPrefWidth(280);
                        }
                    };
                    taskName.setStyle("-fx-font-size: 14px; -jfx-checked-color: #99aaaa; -jfx-unchecked-color: " +
                            "#bbbbbb; -fx-text-fill: #232323");

                    if (!TaskUtils.isTaskAndSubtaskContainUser(task)) {
                        taskCheckBox.setDisable(true);
                        taskName.setStyle("-fx-font-size: 14px; -fx-text-fill: #777777");
                    } else {
                        taskName.setStyle("-fx-font-size: 14px; -fx-text-fill: #232323");
                    }

                    taskItem.getChildren().add(taskCheckBox);
                    taskItem.getChildren().add(taskName);

                    taskCheckBox.setOnMouseClicked(mouseEvent -> {
                        JSONObject newBuffer = JsonUtils.getBuffer();
                        newBuffer.put("idTask", task.getIdTask());
                        JsonUtils.setBuffer(newBuffer);
                        task.setStatus("unfinished");
                        TaskUtils.updateTaskList(task);
                        updateProjectListArea();
                        updateTaskListArea();
                        updateTaskDetailArea();
                    });
                    taskName.setOnMouseClicked(mouseEvent -> {
                        JSONObject newBuffer = JsonUtils.getBuffer();
                        newBuffer.put("idTask", task.getIdTask());
                        JsonUtils.setBuffer(newBuffer);
                        updateTaskDetailArea();
                    });
                    taskItem.setOnMouseClicked(mouseEvent -> {
                        JSONObject newBuffer = JsonUtils.getBuffer();
                        newBuffer.put("idTask", task.getIdTask());
                        JsonUtils.setBuffer(newBuffer);
                        updateTaskDetailArea();
                    });

                    // 设置任务截止日期
                    long timestamp = task.getDeadlineTime();
                    if (timestamp != -1) {
                        Label taskDeadline =
                                new Label(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
                                        .toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) {
                                    {
                                        setLayoutX(315);
                                        setLayoutY(13);
                                    }
                                };
                        taskDeadline.setStyle("-fx-text-fill: #bbbbbb; -fx-font-size: 14px; -fx-font-style: italic");
                        taskItem.getChildren().add(taskDeadline);
                    }

                    AnchorPane decorationLine = new AnchorPane() {
                        {
                            setLayoutX(0);
                            setLayoutY(40);
                            setPrefWidth(405);
                            setPrefHeight(1);
                        }
                    };
                    decorationLine.setStyle("-fx-background-color: #eeeeee");
                    taskItem.getChildren().add(decorationLine);

                    taskListArea.getChildren().add(taskItem);
                }

            }

            taskListScrollArea.setContent(taskListArea);
            taskListScrollArea.setFitToWidth(true);
            taskListScrollArea.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            taskListScrollArea.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
    }

    /**
     * 更新任务详细栏的内容
     */
    private void updateTaskDetailArea() {
        for (Node node : taskDetailArea.getChildren()) {
            node.setVisible(true);
        }
        wrongTaskTitleTip.setVisible(false);

        JSONObject buffer = JsonUtils.getBuffer();
        Task task = TaskUtils.getTaskByIdTask((String) buffer.get("idTask"));
        taskCreatorUsername.setText(UserUtils.getUserByID(task.getIdUser()).getUsername());
        taskTitle.setText(task.getTaskTitle());
        if (!task.getDescription().equals("NULL")) {
            taskDescription.setText(task.getDescription());
        } else {
            taskDescription.setText("");
        }
        if (task.getDeadlineTime() != -1) {
            taskDeadline.setValue(OtherUtils.conventLongToLocalDate(task.getDeadlineTime()));
        }

        Task writingTask = TaskUtils.getWritingSubtask();
        Task proofTask = TaskUtils.getProofreadSubtask();


        if (writingTask != null) {
            writerSelectButton.setText(UserUtils.getUserByID(writingTask.getIdUser()).getUsername());
            writingTaskButton.setSelected(writingTask.getStatus().equals("finished"));
        } else {
            writerSelectButton.setText("Click and select writer");
            writingTaskButton.setSelected(false);
        }
        if (proofTask != null) {
            proofreaderSelectButton.setText(UserUtils.getUserByID(proofTask.getIdUser()).getUsername());
            proofreadingTaskButton.setSelected(proofTask.getStatus().equals("finished"));
        } else {
            proofreaderSelectButton.setText("Select proofreader");
            proofreadingTaskButton.setSelected(false);
        }


        writingTaskButton.setOnMouseClicked(event -> {
            if (writingTask != null && writingTask.getStatus().equals("unfinished")) {
                writingTask.setStatus("finished");
                TaskUtils.updateTaskList(writingTask);
                updateTaskDetailArea();
            } else if (writingTask != null && writingTask.getStatus().equals("finished")) {
                writingTask.setStatus("unfinished");
                TaskUtils.updateTaskList(writingTask);
                updateTaskDetailArea();
            }
        });
        proofreadingTaskButton.setOnMouseClicked(event -> {
            if (proofTask != null && proofTask.getStatus().equals("unfinished")) {
                proofTask.setStatus("finished");
                TaskUtils.updateTaskList(proofTask);
                updateTaskDetailArea();
            } else if (proofTask != null && proofTask.getStatus().equals("finished")) {
                proofTask.setStatus("unfinished");
                TaskUtils.updateTaskList(proofTask);
                updateTaskDetailArea();
            }
        });

        if (!task.getIdUser().equals(buffer.get("idUser")) && !buffer.getString("idUser")
                .equals(ProjectUtils.getProjectByIdProject(buffer.getString("idProject")).getMasterIdUser())) {
            for (Node node : taskDetailArea.getChildren()) {
                node.setDisable(true);
            }
            subtaskArea.setDisable(false);
            deleteTaskButton.setVisible(false);
            writingTaskButton.setDisable(writingTask == null || !buffer.getString("idUser").equals(writingTask.getIdUser()));

            proofreadingTaskButton.setDisable(proofTask == null || !buffer.getString("idUser").equals(proofTask.getIdUser()));
        } else {
            for (Node node : taskDetailArea.getChildren()) {
                node.setDisable(false);
            }
            writingTaskButton.setDisable(writingTask == null);
            proofreadingTaskButton.setDisable(proofTask == null);
            deleteTaskButton.setVisible(true);
        }
    }

    /**
     * 更新用户首字母的内容
     */
    private void updateFirstLetter() {
        JSONObject buffer = JsonUtils.getBuffer();
        avatarLetter.setText(buffer.get("username").toString().toUpperCase().charAt(0) + "");
    }

    /**
     * IDEA自动生成的函数，提取了初始化个人资料页的重复代码
     */
    private void changeProfileCheck(AtomicBoolean isUsernameInputValid, AtomicBoolean isEmailInputValid,
                                    AtomicBoolean isTelephoneInputValid, Label wrongInputTip, JFXButton savaButton) {
        savaButton.setDisable(!isUsernameInputValid.get() || !isEmailInputValid.get() || !isTelephoneInputValid.get());
        if (!isUsernameInputValid.get()) {
            wrongInputTip.setText("Username exists or cannot be empty");
            wrongInputTip.setVisible(true);
        } else if (!isEmailInputValid.get()) {
            wrongInputTip.setText("Email address is invalid");
            wrongInputTip.setVisible(true);
        } else if (!isTelephoneInputValid.get()) {
            wrongInputTip.setText("Telephone number is invalid");
            wrongInputTip.setVisible(true);
        } else {
            wrongInputTip.setVisible(false);
        }
    }

    /**
     * 更新选择用户列表
     *
     * @param userListArea
     */
    private void updateUserList(AnchorPane userListArea, String search) {
        userListArea.getChildren().clear();
        int i = 0;
        for (User user : UserUtils.getSubtaskSelectUserList(search)) {
            int finalI = i;
            AnchorPane userItem = new AnchorPane() {
                {
                    setLayoutX(0);
                    setLayoutY(55 * finalI);
                    setPrefHeight(55);
                    setPrefWidth(250);
                }
            };
            userItem.getStyleClass().add("item-container");

            AnchorPane userAvatarArea = new AnchorPane() {
                {
                    setLayoutX(10);
                    setLayoutY(10);
                    setPrefHeight(35);
                    setPrefWidth(35);
                }
            };

            userAvatarArea.setStyle("-fx-background-color: #c8dede; -fx-background-radius: 50px");
            Label userFirstLetter = new Label(user.getUsername().toUpperCase().toCharArray()[0] + "") {
                {
                    setLayoutX(0);
                    setLayoutY(7);
                    setPrefWidth(35);
                }
            };
            userFirstLetter.setStyle("-fx-text-fill: #99aaaa; -fx-text-alignment: center; -fx-alignment: center; " +
                    "-fx-font-size: 16px; -fx-font-weight: bold");

            Label username = new Label(user.getUsername()) {
                {
                    setLayoutX(52);
                    setLayoutY(10);
                }
            };
            username.setStyle("-fx-text-fill: #777777; -fx-font-size: 14px; ");

            Label statusTask = new Label(TaskUtils.getStatusOfUserInProjectByIdUser(user.getIdUser())) {
                {
                    setLayoutX(52);
                    setLayoutY(31);
                }
            };
            statusTask.setStyle("-fx-text-fill: #999999; -fx-font-size: 12px; ");

            AnchorPane decorationLine = new AnchorPane() {
                {
                    setLayoutX(0);
                    setLayoutY(54);
                    setPrefHeight(1);
                    setPrefWidth(250);
                }
            };
            decorationLine.getStyleClass().add("decoration-line");

            userItem.setOnMouseClicked(event -> {
                JSONObject buffer = JsonUtils.getBuffer();
                Task task = null;
                if (buffer.getString("typeSubtask").equals("proofreading task")){
                    task = TaskUtils.getProofreadSubtask();
                }else if (buffer.getString("typeSubtask").equals("writing task")){
                    task = TaskUtils.getWritingSubtask();
                }
                if (task == null){
                    task = new Task();
                    task.setIdTask(String.valueOf(TaskUtils.getNextTaskId(TaskUtils.getTaskList())));
                }
                task.setIdUser(user.getIdUser());
                task.setTaskTitle(buffer.getString("typeSubtask"));
                task.setIdParentTask(buffer.getString("idTask"));
                task.setIdProject(buffer.getString("idProject"));
                task.setDescription(buffer.getString("typeSubtask"));
                TaskUtils.updateTaskList(task);
                updateTaskDetailArea();
                selectExecutorPopUp.hide();
            });

            userAvatarArea.getChildren().add(userFirstLetter);
            userItem.getChildren().add(userAvatarArea);
            userItem.getChildren().add(username);
            userItem.getChildren().add(statusTask);
            userItem.getChildren().add(decorationLine);
            userListArea.getChildren().add(userItem);
            i++;
        }
    }

    /**
     * 将任务列表区域还原为初始设置
     */
    private void clearTaskListArea() {
        projectTitle.setDisable(true);
        projectTitle.setText("Project Name");
        copyProjectCodeButton.setDisable(true);
        editProjectButton.setDisable(true);
        newTaskInput.setDisable(true);
        deleteTaskButton.setDisable(true);
        taskListUnselectedTip.setVisible(true);
        projectNoTaskTip.setVisible(false);
        wrongTaskTitleTip.setVisible(false);
        taskListScrollArea.setVisible(false);
    }

    /**
     * 将任务详细区域还原为初始设置
     */
    private void clearTaskDetailArea() {
        for (Node node : taskDetailArea.getChildren()) {
            node.setVisible(false);
        }
    }
}
