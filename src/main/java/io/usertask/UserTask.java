package io.usertask;


/**
 * 用户与任务数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class UserTask {
    private String idUser;
    private String idTask;

    /**
     * 初始化用户数据
     */
    public UserTask() {
        idUser = "NULL";
        idTask = "NULL";
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
     * @return 将UserTask对象用字符串表示
     */
    public String toString() {

        return "----------------------------\n" +
                "idUser: " + idUser + "\n" +
                "idTask: " + idTask + "\n" +
                "----------------------------\n";

    }
}
