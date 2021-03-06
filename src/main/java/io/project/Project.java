package io.project;

import utils.ProjectUtils;

import java.util.Date;

/**
 * Project数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class Project{
    private String idProject;
    private String masterIdUser;
    private String projectName;
    private String description;
    private long createTime;
    private long deadlineTime;
    private int progressSituation;

    /**
     * 初始化任务数据
     */
    public Project() {
        idProject = "NULL";
        masterIdUser = "NULL";
        projectName = "NULL";
        description = "NULL";
        createTime = new Date().getTime();
        deadlineTime = -1;
        progressSituation = 0;
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
     * Gets masterIdUser
     *
     * @return value of masterIdUser
     */
    public String getMasterIdUser() {
        return masterIdUser;
    }

    /**
     * Set masterIdUser
     *
     * @param masterIdUser set value of masterIdUser
     */
    public void setMasterIdUser(String masterIdUser) {
        this.masterIdUser = masterIdUser;
    }

    /**
     * Gets projectName
     *
     * @return value of projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Set projectName
     *
     * @param projectName set value of projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
     * Gets progressSituation
     *
     * @return value of progressSituation
     */
    public int getProgressSituation() {
        return progressSituation;
    }

    /**
     * Set progressSituation
     *
     * @param progressSituation set value of progressSituation
     */
    public void setProgressSituation(int progressSituation) {
        this.progressSituation = progressSituation;
    }

    /**
     * @return 将Project对象用字符串表示
     */
    public String toString() {

        return "----------------------------\n" +
                "idProject: " + idProject + "\n" +
                "master id user: " + masterIdUser + "\n" +
                "project name: " + projectName + "\n" +
                "description: " + description + "\n" +
                "create time: " + createTime + "\n" +
                "deadline time: " + deadlineTime + "\n" +
                "----------------------------\n";

    }

}
