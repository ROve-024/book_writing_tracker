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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainPage {
    private final Popup popup = new Popup();
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
        sortProjectByCreateTimeAction();
        initialPopupProjectEditor();
        projectTitle.setDisable(true);
        editProjectButton.setDisable(true);
        newTaskInput.setDisable(true);
        taskListUnselectedTip.setVisible(true);
        projectNoTaskTip.setVisible(false);
        wrongTaskTitleTip.setVisible(false);
        clearTaskDetailArea();

        newProjectButton.setOnMouseClicked(event -> {
            if (!popup.isShowing()) {
                JSONObject projectEditorBuffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer" +
                        "/mainPageBuffer.json");
                String option = (String) projectEditorBuffer.get("option");
                if (option.equals("edit")) {
                    projectEditorBuffer.put("option", "create");
                    JsonUtils.saveJsonToFile(projectEditorBuffer, "src/main/resources/buffer/mainPageBuffer.json");
                    initialPopupProjectEditor();
                }
                popup.show(newProjectButton.getScene().getWindow());
            }
        });

        editProjectButton.setOnMouseClicked(event -> {
            if (!popup.isShowing()) {
                JSONObject projectEditorBuffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer" +
                        "/mainPageBuffer.json");
                String option = (String) projectEditorBuffer.get("option");
                projectEditorBuffer.put("option", "edit");
                JsonUtils.saveJsonToFile(projectEditorBuffer, "src/main/resources/buffer/mainPageBuffer.json");
                initialPopupProjectEditor();
                popup.show(newProjectButton.getScene().getWindow());
            }
        });

        newTaskInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String idProject = (String) JsonUtils.getJsonObjectFromFile("src/main/resources/buffer" +
                        "/mainPageBuffer.json").get("idProject");
                Task task = new Task();
                task.setIdTask(String.valueOf(TaskUtils.getNextTaskId(TaskUtils.getTaskList())));
                task.setIdUser(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile("src/main/resources" +
                        "/buffer/infoLogin.json").get("username")));
                task.setTaskTitle(newTaskInput.getText());
                task.setIdProject(idProject);
                TaskUtils.createTask(task);
                updateTaskListArea(idProject);
                JSONObject buffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/mainPageBuffer.json");
                if (buffer.get("sortMethod").toString().equals("createTime")) {
                    sortProjectByCreateTimeAction();
                } else if (buffer.get("sortMethod").toString().equals("deadline")) {
                    sortProjectByDeadLineAction();
                } else if (buffer.get("sortMethod").toString().equals("schedule")) {
                    sortProjectByScheduleAction();
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
        JSONObject buffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/mainPageBuffer.json");
        buffer.put("sortMethod", "deadline");
        JsonUtils.saveJsonToFile(buffer, "src/main/resources/buffer/mainPageBuffer.json");
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
        JSONObject buffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/mainPageBuffer.json");
        buffer.put("sortMethod", "createTime");
        JsonUtils.saveJsonToFile(buffer, "src/main/resources/buffer/mainPageBuffer.json");
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
        JSONObject buffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/mainPageBuffer.json");
        buffer.put("sortMethod", "schedule");
        JsonUtils.saveJsonToFile(buffer, "src/main/resources/buffer/mainPageBuffer.json");
        updateProjectListArea(projectList);
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
            } else if (Math.round(100 * taskSituation) < 10){
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
                JSONObject projectEditorBuffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer" +
                        "/mainPageBuffer.json");
                projectEditorBuffer.put("idProject", projectId.getText());
                JsonUtils.saveJsonToFile(projectEditorBuffer, "src/main/resources/buffer/mainPageBuffer.json");
                clearTaskDetailArea();
                updateTaskListArea(projectId.getText());
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
     * 更新任务列表栏的内容
     */
    private void updateTaskListArea(String idProject) {
        User user = UserUtils.getUserByID(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile("src/main/resources" +
                "/buffer/infoLogin.json").get("username")));

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
                            setPrefWidth(305);
                        }
                    };
                    taskCheckBox.setStyle("-fx-font-size: 14px; -jfx-checked-color: #99aaaa; -jfx-unchecked-color: " +
                            "#bbbbbb; -fx-text-fill: #232323");
                    taskCheckBox.setText(task.getTaskTitle());
                    if (!task.getIdUser().equals(user.getIdUser())) {
                        taskCheckBox.setDisable(true);
                    }
                    taskItem.getChildren().add(taskCheckBox);

                    taskCheckBox.setOnMouseClicked(mouseEvent -> {
                        JSONObject mainPageBuffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer" +
                                "/mainPageBuffer.json");
                        mainPageBuffer.put("idTask", task.getIdTask());

                        if (task.getIdUser().equals(user.getIdUser())) {
                            task.setStatus("finished");
                            TaskUtils.updateTaskList(task);
                            JsonUtils.saveJsonToFile(mainPageBuffer, "src/main/resources/buffer/mainPageBuffer.json");
                            updateTaskListArea((String) mainPageBuffer.get("idProject"));

                            JSONObject buffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/mainPageBuffer.json");
                            if (buffer.get("sortMethod").toString().equals("createTime")) {
                                sortProjectByCreateTimeAction();
                            } else if (buffer.get("sortMethod").toString().equals("deadline")) {
                                sortProjectByDeadLineAction();
                            } else if (buffer.get("sortMethod").toString().equals("schedule")) {
                                sortProjectByScheduleAction();
                            }

                            updateTaskDetailArea();
                        }
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
                                        setLayoutY(3);
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
                            setPrefWidth(305);
                        }
                    };
                    taskCheckBox.setSelected(true);
                    taskCheckBox.setStyle("-fx-font-size: 14px; -jfx-checked-color: #99aaaa; -jfx-unchecked-color: " +
                            "#bbbbbb; -fx-text-fill: #232323");
                    taskCheckBox.setText(task.getTaskTitle());
                    if (!task.getIdUser().equals(user.getIdUser())) {
                        taskCheckBox.setDisable(true);
                    }
                    taskItem.getChildren().add(taskCheckBox);

                    taskCheckBox.setOnMouseClicked(mouseEvent -> {
                        JSONObject mainPageBuffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer" +
                                "/mainPageBuffer.json");
                        mainPageBuffer.put("idTask", task.getIdTask());
                        if (task.getIdUser().equals(user.getIdUser())) {
                            task.setStatus("unfinished");
                            TaskUtils.updateTaskList(task);
                            JsonUtils.saveJsonToFile(mainPageBuffer, "src/main/resources/buffer/mainPageBuffer.json");
                            updateTaskListArea((String) mainPageBuffer.get("idProject"));

                            JSONObject buffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/mainPageBuffer.json");
                            if (buffer.get("sortMethod").toString().equals("createTime")) {
                                sortProjectByCreateTimeAction();
                            } else if (buffer.get("sortMethod").toString().equals("deadline")) {
                                sortProjectByDeadLineAction();
                            } else if (buffer.get("sortMethod").toString().equals("schedule")) {
                                sortProjectByScheduleAction();
                            }
                        }
                    });

                    // 设置任务截止日期
                    long timestamp = task.getDeadlineTime();
                    if (timestamp != -1) {
                        Label taskDeadline =
                                new Label(Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
                                        .toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))) {
                                    {
                                        setLayoutX(315);
                                        setLayoutY(3);
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
        for(Node node : taskDetailArea.getChildren()){
            node.setVisible(true);
        }
        wrongTaskTitleTip.setVisible(false);

        JSONObject buffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer/mainPageBuffer.json");
        Task task = TaskUtils.getTaskByIdTask((String) buffer.get("idTask"));
        taskTitle.setText(task.getTaskTitle());
        if (!task.getDescription().equals("NULL")){
            taskDescription.setText(task.getDescription());
        }
        if (task.getDeadlineTime() != -1){
            taskDeadline.setValue(OtherUtils.conventLongToLocalDate(task.getDeadlineTime()));
        }



    }

    /**
     * 新建和编辑项目窗口
     */
    private void initialPopupProjectEditor() {
        popup.setAutoHide(true);

        JSONObject projectEditorBuffer = JsonUtils.getJsonObjectFromFile("src/main/resources/buffer" +
                "/mainPageBuffer.json");
        String option = (String) projectEditorBuffer.get("option");

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
        projectTitle.setPromptText("Project title");
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

        Label wrongProjectTitleTip = new Label("Project title cannot be empty.") {
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

        projectTitle.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
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

        if (option.equals("edit")) {
            String projectId = (String) projectEditorBuffer.get("idProject");
            Project project = ProjectUtils.getProjectByIdProject(projectId);
            projectTitle.setText(project.getProjectName());
            projectDescription.setText(project.getDescription());
            LocalDate date = OtherUtils.conventLongToLocalDate(project.getDeadlineTime());
            datePicker.setValue(date);
            String idUser = UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile("src/main/resources" +
                    "/buffer/infoLogin.json").get("username"));
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
            } else {
                projectTitle.setDisable(false);
                projectDescription.setDisable(false);
                datePicker.setDisable(false);
                saveButton.setDisable(false);
            }

        }

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    datePicker.getValue().equals(null);
                    if (projectTitle.getText().isEmpty()) {
                        wrongProjectTitleTip.setVisible(true);
                        wrongProjectDeadlineTip.setVisible(false);
                    } else {
                        wrongProjectTitleTip.setVisible(false);
                        Project project = new Project();
                        project.setIdProject(String.valueOf(ProjectUtils.getNextNewProjectId(ProjectUtils.getProjectList())));
                        project.setMasterIdUser(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile(
                                "src/main/resources/buffer/infoLogin.json").get("username")));
                        project.setProjectName(projectTitle.getText());
                        project.setDescription(projectDescription.getText());
                        project.setDeadlineTime(OtherUtils.conventLocalDateToLong(datePicker.getValue()));
                        if (option.equals("create")) {
                            ProjectUtils.createProject(project);
                        } else if (option.equals("edit")) {
                            ProjectUtils.updateProjectList(project);
                        }

                        UserProject userProject = new UserProject();
                        userProject.setIdProject(project.getIdProject());
                        userProject.setIdUser(UserUtils.getIDByUsername((String) JsonUtils.getJsonObjectFromFile(
                                "src/main/resources/buffer/infoLogin.json").get("username")));
                        UserProjectUtils.createUserProject(userProject);

                        projectTitle.setText("");
                        projectDescription.setText("");
                        datePicker.setValue(null);
                        popup.hide();

                        sortProjectByCreateTimeAction();
                    }
                } catch (NullPointerException ignored) {
                    wrongProjectDeadlineTip.setVisible(true);
                    wrongProjectTitleTip.setVisible(false);
                }

            }
        });

        popup.getContent().add(projectEditorArea);

    }

    private void clearTaskDetailArea(){
        for(Node node : taskDetailArea.getChildren()){
            node.setVisible(false);
        }
    }
}
