package io.userproject;

/**
 * 用户与项目数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class UserProject {
    private String idUser;
    private String idProject;

    /**
     * 初始化用户数据
     */
    public UserProject() {
        idUser = "NULL";
        idProject = "NULL";
    }

    /**
     * 初始化用户数据
     */
    public UserProject(String idUser, String idProject) {
        this.idUser = idUser;
        this.idProject = idProject;
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
     * @return 将UserProject对象用字符串表示
     */
    public String toString() {

        return "----------------------------\n" +
                "idUser: " + idUser + "\n" +
                "idProject: " + idProject + "\n" +
                "----------------------------\n";

    }
}
