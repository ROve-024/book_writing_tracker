package utils;

import io.task.Task;
import io.user.User;
import io.usertask.UserTask;
import io.usertask.UserTaskReadWrite;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理UserTask类对象的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class UserTaskUtils {
    /**
     * 从文件中获取UserTaskList
     *
     * @return 储存的用户-任务关系列表
     */
    public static List<UserTask> getUserTaskList() {
        return UserTaskReadWrite.readUserTaskXML();
    }

    /**
     * 将用户-任务关系列表保存到文件
     *
     * @param userTaskList 要写入文件的用户-任务列表
     */
    public static void writeUserTaskXML(List<UserTask> userTaskList) {
        new UserTaskReadWrite().writeUserTaskXML(userTaskList);
    }

    /**
     * 将新建的用户-任务关系保存到本地
     *
     * @param userTask 新建的用户-任务关系
     */
    public static void createUserTask(UserTask userTask) {
        List<UserTask> userTaskList = getUserTaskList();
        userTaskList.add(userTask);
        writeUserTaskXML(userTaskList);
    }

    /**
     * 获取参与某任务的所有用户的list
     *
     * @param taskId 任务id
     * @return 返回参与该任务的所有用户列表
     */
    public static List<User> getUserListByTaskId(String taskId) {
        List<User> userList = new ArrayList<>();
        for (UserTask value : getUserTaskList()) {
            if (value.getIdTask().equals(taskId)) {
                userList.add(UserUtils.getUserByID(value.getIdUser()));
            }
        }
        return userList;
    }

    /**
     * 获取某用户参与的所有任务的list
     *
     * @param userId 用户id
     * @return 返回某用户的任务列表
     */
    public static List<Task> getTaskListByUserId(String userId) {
        List<Task> taskList = new ArrayList<>();
        for (UserTask value : getUserTaskList()) {
            if (value.getIdUser().equals(userId)) {
                taskList.add(TaskUtils.getTaskByIdTask(value.getIdTask()));
            }
        }
        return taskList;
    }

    /**
     * 使用idTask删除UserTask
     *
     * @param idTask task id
     */
    public static void deleteUserTaskByIdTask(String idTask) {
        List<UserTask> userTaskList = getUserTaskList();
        userTaskList.removeIf(value -> value.getIdTask().equals(idTask));
        writeUserTaskXML(userTaskList);
    }

}
