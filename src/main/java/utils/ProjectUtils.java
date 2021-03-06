package utils;

import io.project.Project;
import io.project.ProjectReadWrite;
import io.task.Task;
import io.user.User;
import io.userproject.UserProject;
import javafx.stage.Popup;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
    public static int getNextNewProjectId(List<Project> projectList) {
        int cnt = 0;
        Project project;
        for (Project value : projectList) {
            project = value;
            int temp = Integer.parseInt(project.getIdProject());
            if (cnt <= temp) {
                cnt = temp;
            }
        }
        return cnt + 1;
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
        Iterator<Project> iterator = projectList.iterator();
        while (iterator.hasNext()) {
            Project project = iterator.next();
            if (project.getIdProject().equals(idProject)) {
                iterator.remove();
                break;
            }
        }

        TaskUtils.deleteTaskByIdProject(idProject);
        UserProjectUtils.deleteUserProjectByIdProject(idProject);
        writeProjectXML(projectList);
    }

    /**
     * 使用idProject查询项目完成情况
     *
     * @param idProject project id
     */
    public static double getProjectProcessByIdProject(String idProject) {
        int finished = TaskUtils.getFinishedTaskNumberByIdProject(idProject);
        int total = TaskUtils.getAllTaskNumberByIdProject(idProject);
        if (total == 0) {
            total = 1;
        }
        return (1.0 * finished) / total;
    }

    /**
     * 使用截止时间顺序排序
     *
     * @param projectList 待排序的项目列表
     */
    public static void sortTaskByDeadlineOrder(List<Project> projectList) {
        projectList.sort(Comparator.comparing(Project::getDeadlineTime));
    }

    /**
     * 使用创建时间顺序排序
     *
     * @param projectList 待排序的项目列表
     */
    public static void sortTaskByCreateTimeOrder(List<Project> projectList) {
        projectList.sort(Comparator.comparing(Project::getCreateTime));
        Collections.reverse(projectList);
    }

    /**
     * 使用项目进度顺序排序
     *
     * @param projectList 待排序的项目列表
     */
    public static void sortTaskByScheduleOrder(List<Project> projectList) {
        for (Project project : projectList) {
            project.setProgressSituation((int) Math.round(100 * ProjectUtils.getProjectProcessByIdProject(project.getIdProject())));
        }
        projectList.sort(Comparator.comparing(Project::getProgressSituation));
    }


    public static String getIdProjectByCode(String code) {
        String result = "-1";
        for (Project value : getProjectList()) {
            if (OtherUtils.encryptByMD5(value.getIdProject()).equals(code)) {
                result = value.getIdProject();
                break;
            }
        }
        if (!result.equals("-1")){
            String idUser = JsonUtils.getBuffer().getString("idUser");
            for(User value : UserProjectUtils.getUserListByProjectId(result)){
                if (value.getIdUser().equals(idUser)) {
                    result = "-2";
                    break;
                }
            }
        }
        return result;
    }

}
