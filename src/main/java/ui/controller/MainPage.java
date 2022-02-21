package ui.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import io.project.Project;
import io.task.Task;
import io.user.User;
import io.userproject.UserProject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainPage {
    private final Popup projectEditorPopUp = new Popup();
    private final Popup confirmDeletePopup = new Popup();
    @FXML
    private JFXButton homepageButton;
    @FXML
    private JFXButton myTaskButton;
    @FXML
    private JFXButton recycleBinButton;
    @FXML
    private AnchorPane titleBar;
    @FXML
    private AnchorPane taskListUnselectedTip;
    @FXML
    private AnchorPane taskDetailArea;
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
    private Label taskCreaterUsername;
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
        initialBuffer.put("projectEditorStatus", "initial");
        JsonUtils.setBuffer(initialBuffer);
        sortProjectByCreateTimeAction();
        initialPopupProjectEditor();
        clearTaskListArea();
        clearTaskDetailArea();
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
                String projectEditorOption = (String) projectEditorBuffer.get("projectEditorOption");
                projectEditorBuffer.put("projectEditorOption", "edit");
                JsonUtils.setBuffer(projectEditorBuffer);
                initialPopupProjectEditor();
                projectEditorPopUp.show(newProjectButton.getScene().getWindow());
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
        initialize();
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
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("page", "recycleBin");
        JsonUtils.setBuffer(buffer);

        Parent root = null;
        try {
            root = FXMLLoader.load(new File("src/main/java/ui/fxml/RecycleBin.fxml").toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        WindowsUtils.initial(root, (Stage) projectTitle.getScene().getWindow());
        projectTitle.getScene().setRoot(root);
    }

    @FXML
    protected void logoutButtonAction() {
        JSONObject buffer = JsonUtils.getBuffer();
        buffer.put("userStatus", OtherUtils.encryptByMD5("false"));
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
    protected void deleteTaskButtonAction(){
        JSONObject buffer = JsonUtils.getBuffer();
        TaskUtils.deleteTaskByIdTask(buffer.get("idTask").toString());
        updateProjectListArea();
        updateTaskListArea();
        clearTaskDetailArea();
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
        taskCreaterUsername.setText(UserUtils.getUserByID(task.getIdUser()).getUsername());
        taskTitle.setText(task.getTaskTitle());
        if (!task.getDescription().equals("NULL")) {
            taskDescription.setText(task.getDescription());
        } else {
            taskDescription.setText("");
        }
        if (task.getDeadlineTime() != -1) {
            taskDeadline.setValue(OtherUtils.conventLongToLocalDate(task.getDeadlineTime()));
        }

        if (!task.getIdUser().equals(buffer.get("idUser"))) {
            for (Node node : taskDetailArea.getChildren()) {
                node.setDisable(true);
            }
            deleteTaskButton.setVisible(true);
        } else {
            for (Node node : taskDetailArea.getChildren()) {
                node.setDisable(false);
            }
            deleteTaskButton.setVisible(false);
        }
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
        projectEditorArea.setEffect(new DropShadow(10, 0, 2, new Color(0.86, 0.86, 0.86, 1)));

        JFXTextField projectTitle = new JFXTextField() {
            {
                setLayoutX(15);
                setLayoutY(20);
                setPrefWidth(270);
                setPrefHeight(25);
            }
        };
        projectTitle.getStyleClass().add("text-field-without-line");
        projectTitle.setPromptText("Projct name");
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

        Label wrongProjectTitleTip = new Label("Projct name cannot be empty.") {
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

        if (projectEditorOption.equals("edit") && !buffer.get("projectEditorStatus").toString().equals("initial")) {
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

        } else if(!projectEditorOption.equals("edit") ){
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
                if (projectEditorOption.equals("create")) {
                    ProjectUtils.createProject(project);
                } else if (projectEditorOption.equals("edit")) {
                    ProjectUtils.updateProjectList(project);
                }

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
            if (!confirmDeletePopup.isShowing()) {
                initialConfirmDeletePopUp();
                confirmDeletePopup.show(homepageButton.getScene().getWindow());
                projectEditorPopUp.hide();
            }
        });

        projectEditorPopUp.getContent().add(projectEditorArea);

    }

    /**
     * 确定删除项目界面
     */
    private void initialConfirmDeletePopUp() {
        confirmDeletePopup.setAutoHide(true);
        AnchorPane deleteProjectArea = new AnchorPane() {
            {
                setPrefWidth(420);
                setPrefHeight(120);
            }
        };
        deleteProjectArea.getStylesheets().add("css/styles.css");
        deleteProjectArea.setStyle("-fx-background-color: #f5f5f5");
        deleteProjectArea.setEffect(new DropShadow(10, 0, 2, new Color(0.86, 0.86, 0.86, 1)));

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
                if(confirmDeleteProjectInput.getText().equals(project.getProjectName())){
                    confirmDeleteProjectButton.setDisable(false);
                }
            }
        });

        confirmDeleteProjectButton.setOnAction(event -> {
            ProjectUtils.deleteProjectByIdProject(project.getIdProject());
            updateProjectListArea();
            clearTaskListArea();
            clearTaskDetailArea();
        });

        confirmDeletePopup.getContent().add(deleteProjectArea);
    }

    private void clearTaskListArea(){
        projectTitle.setDisable(true);
        editProjectButton.setDisable(true);
        newTaskInput.setDisable(true);
        deleteTaskButton.setDisable(true);
        taskListUnselectedTip.setVisible(true);
        projectNoTaskTip.setVisible(false);
        wrongTaskTitleTip.setVisible(false);
        taskListScrollArea.setVisible(false);
    }

    private void clearTaskDetailArea() {
        for (Node node : taskDetailArea.getChildren()) {
            node.setVisible(false);
        }
    }
}
