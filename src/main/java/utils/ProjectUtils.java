package utils;

import io.project.Project;
import io.project.ProjectReadWrite;

import java.util.List;

/**
 * 处理Project类对象的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class ProjectUtils {
    /**
     * 从文件中获取ProjectList
     *
     * @return 储存的项目列表
     */
    public static List<Project> getProjectList() {
        return ProjectReadWrite.readProjectXML();
    }

    /**
     * 将项目列表保存到文件
     *
     * @param projectList 要写入文件的项目列表
     */
    public static void writeProjectXML(List<Project> projectList) {
        new ProjectReadWrite().writeProjectXML(projectList);
    }

    /**
     * 获取最大的项目id值
     *
     * @param projectList 已有的任务列表
     * @return 返回最大id
     */
    public static int maxProjectId(List<Project> projectList) {
        int cnt = 0;
        Project project;
        for (Project value : projectList) {
            project = value;
            int temp = Integer.parseInt(project.getIdProject());
            if (cnt <= temp) {
                cnt = temp;
            }
        }
        return cnt;
    }

    /**
     * 将新建的项目保存到本地
     *
     * @param project 新建的项目
     */
    public static void createProject(Project project) {
        List<Project> projectList = getProjectList();
        projectList.add(project);
        writeProjectXML(projectList);
    }

    /**
     * 更新项目数据，并更新相应文件
     *
     * @param project 需要更新的项目
     */
    public static void updateProjectList(Project project) {
        List<Project> projectList = getProjectList();
        projectList.removeIf(temp -> temp.getIdProject().equals(project.getIdProject()));
        projectList.add(project);
        writeProjectXML(projectList);
    }

    /**
     * 使用idProject查找项目
     *
     * @param idProject project id
     * @return 返回相应的项目列表
     */
    public static Project getProjectByIdProject(String idProject) {
        Project project = null;
        for (Project value : getProjectList()) {
            if (value.getIdProject().equals(idProject)) {
                project = value;
                break;
            }
        }
        return project;
    }

    /**
     * 使用idProject删除
     *
     * @param idProject project id
     */
    public static void deleteProjectByIdProject(String idProject) {
        List<Project> projectList = getProjectList();
        for (Project value : projectList) {
            if (value.getIdProject().equals(idProject)) {
                projectList.remove(value);
                break;
            }
        }
        TaskUtils.deleteTaskByIdProject(idProject);
        UserProjectUtils.deleteUserProjectByIdProject(idProject);
        writeProjectXML(projectList);
    }


}
