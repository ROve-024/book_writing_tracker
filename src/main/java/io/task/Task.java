package io.task;

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
    private long createTime;
    private long deadlineTime;
    private String description;

    /**
     * 初始化任务数据
     */
    public Task() {
        idTask = "NULL";
        idParentTask = "NULL";
        idProject = "NULL";
        idUser = "NULL";
        status = "NULL";
        createTime = -1;
        deadlineTime = -1;
        description = "NULL";
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
     * @return 将task对象用字符串表示
     */
    public String toString() {

        return "----------------------------\n" +
                "idTask: " + idTask + "\n" +
                "idParentTask: " + idParentTask + "\n" +
                "idProject: " + idProject + "\n" +
                "idUser: " + idUser + "\n" +
                "status: " + status + "\n" +
                "create time: " + createTime + "\n" +
                "deadline time" + deadlineTime + "\n" +
                "description" + description + "\n" +
                "----------------------------\n";

    }

}
