package io.task;

import utils.TaskUtils;

import java.util.Date;

/**
 * Task数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class Task {
    private String idTask;
    private String idParentTask;
    private String idProject;
    private String idUser;
    private String status;
    private String taskTitle;
    private long createTime;
    private long deadlineTime;
    private String description;

    /**
     * 初始化任务数据
     */
    public Task() {
        this.idTask = "NULL";
        this.idParentTask = "NULL";
        this.idProject = "";
        this.idUser = "";
        this.status = "unfinished";
        this.taskTitle = "";
        this.createTime = new Date().getTime();
        this.deadlineTime = -1;
        this.description = "NULL";
    }

    /**
     * 创建子任务
     * @param idParentTask 父任务id
     * @param description 任务描述，只有”writing task“和”proofreading task“
     */
    public Task(String idParentTask, String description){
        this.idTask = "NULL";
        this.idParentTask = idParentTask;
        this.idProject = "";
        this.idUser = "";
        this.status = "unfinished";
        this.taskTitle = "";
        this.createTime = new Date().getTime();
        this.deadlineTime = -1;
        this.description = description;
    }

    /**
     * Gets idTask
     *
     * @return value of idTask
     */
    public String getIdTask() {
        return idTask;
    }

    /**
     * Set idTask
     *
     * @param idTask set value of idTask
     */
    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    /**
     * Gets idParentTask
     *
     * @return value of idParentTask
     */
    public String getIdParentTask() {
        return idParentTask;
    }

    /**
     * Set idParentTask
     *
     * @param idParentTask set value of idParentTask
     */
    public void setIdParentTask(String idParentTask) {
        this.idParentTask = idParentTask;
    }

    /**
     * Gets idProject
     *
     * @return value of idProject
     */
    public String getIdProject() {
        return idProject;
    }

    /**
     * Set idProject
     *
     * @param idProject set value of idProject
     */
    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    /**
     * Gets status
     *
     * @return value of status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set status
     *
     * @param status set value of status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets createTime
     *
     * @return value of createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * Set createTime
     *
     * @param createTime set value of createTime
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets deadlineTime
     *
     * @return value of deadlineTime
     */
    public long getDeadlineTime() {
        return deadlineTime;
    }

    /**
     * Set deadlineTime
     *
     * @param deadlineTime set value of deadlineTime
     */
    public void setDeadlineTime(long deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    /**
     * Gets description
     *
     * @return value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     *
     * @param description set value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets idUser
     *
     * @return value of idUser
     */
    public String getIdUser() {
        return idUser;
    }

    /**
     * Set idUser
     *
     * @param idUser set value of idUser
     */
    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    /**
     * Gets taskTitle
     *
     * @return value of taskTitle
     */
    public String getTaskTitle() {
        return taskTitle;
    }

    /**
     * Set taskTitle
     *
     * @param taskTitle set value of taskTitle
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    /**
     * @return 将task对象用字符串表示
     */
    public String toString() {

        return "----------------------------\n" +
                "idTask: " + idTask + "\n" +
                "idParentTask: " + idParentTask + "\n" +
                "idProject: " + idProject + "\n" +
                "idUser: " + idUser + "\n" +
                "status: " + status + "\n" +
                "task title: " + taskTitle + "\n" +
                "create time: " + createTime + "\n" +
                "deadline time" + deadlineTime + "\n" +
                "description" + description + "\n" +
                "----------------------------\n";

    }

}
