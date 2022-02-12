package utils;

import io.project.Project;
import io.user.User;
import io.userproject.UserProject;
import io.userproject.UserProjectReadWrite;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理UserProject类对象的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class UserProjectUtils {
    /**
     * 从文件中获取UserProjectList
     *
     * @return 储存的用户-项目关系列表
     */
    public static List<UserProject> getUserProjectList() {
        return UserProjectReadWrite.readUserProjectXML();
    }

    /**
     * 将用户-项目关系列表保存到文件
     *
     * @param userProjectList 要写入文件的用户-项目关系列表
     */
    public static void writeUserProjectXML(List<UserProject> userProjectList) {
        new UserProjectReadWrite().writeUserProjectXML(userProjectList);
    }

    /**
     * 将新建的用户-项目关系保存到本地
     *
     * @param userProject 新建的用户-项目关系
     */
    public static void createUserProject(UserProject userProject) {
        List<UserProject> userProjectList = getUserProjectList();
        userProjectList.add(userProject);
        writeUserProjectXML(userProjectList);
    }

    /**
     * 获取参与某项目的所有用户的list
     *
     * @param projectId 项目id
     * @return 返回参与该项目的所有用户列表
     */
    public static List<User> getUserListByProjectId(String projectId) {
        List<User> userList = new ArrayList<>();
        for (UserProject value : getUserProjectList()) {
            if (value.getIdProject().equals(projectId)) {
                userList.add(UserUtils.getUserByID(value.getIdUser()));
            }
        }
        return userList;
    }

    /**
     * 获取某用户参与的所有项目的list
     *
     * @param userId 用户id
     * @return 返回某用户的项目列表
     */
    public static List<Project> getProjectListByUserId(String userId) {
        List<Project> projectList = new ArrayList<>();
        for (UserProject value : getUserProjectList()) {
            if (value.getIdUser().equals(userId)) {
                projectList.add(ProjectUtils.getProjectByIdProject(value.getIdProject()));
            }
        }
        return projectList;
    }


    /**
     * 使用idProject删除UserProject数据
     *
     * @param idProject project id
     */
    public static void deleteUserProjectByIdProject(String idProject) {
        List<UserProject> userProjectList = getUserProjectList();
        userProjectList.removeIf(value -> value.getIdProject().equals(idProject));
        writeUserProjectXML(userProjectList);
    }

}
