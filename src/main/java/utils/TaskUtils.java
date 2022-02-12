package utils;

import io.task.Task;
import io.task.TaskReadWrite;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理Task类对象的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 **/
public class TaskUtils {
    /**
     * 从文件中获取TaskList
     *
     * @return 储存的任务列表
     */
    public static List<Task> getTaskList() {
        return TaskReadWrite.readTaskXML();
    }

    /**
     * 将任务列表保存到文件
     *
     * @param taskList 要写入文件的任务列表
     */
    public static void writeTaskXML(List<Task> taskList) {
        new TaskReadWrite().writeTaskXML(taskList);
    }

    /**
     * 获取最大的任务id值
     *
     * @param taskList 已有的任务列表
     * @return 返回最大id
     */
    public static int maxTaskId(List<Task> taskList) {
        int cnt = 0;
        Task task;
        for (Task value : taskList) {
            task = value;
            int temp = Integer.parseInt(task.getIdTask());
            if (cnt <= temp) {
                cnt = temp;
            }
        }
        return cnt;
    }

    /**
     * 将新建的任务保存到本地
     *
     * @param task 新建的任务
     */
    public static void createTask(Task task) {
        List<Task> taskList = getTaskList();
        taskList.add(task);
        writeTaskXML(taskList);
    }

    /**
     * 更新任务数据，并更新相应文件
     *
     * @param task 需要更新的任务
     */
    public static void updateTaskList(Task task) {
        List<Task> taskList = getTaskList();
        taskList.removeIf(temp -> temp.getIdTask().equals(task.getIdTask()));
        taskList.add(task);
        writeTaskXML(taskList);
    }

    /**
     * 使用idTask查找任务
     *
     * @param idTask task id
     * @return 返回相应的任务
     */
    public static Task getTaskByIdTask(String idTask) {
        Task task = null;
        for (Task value : getTaskList()) {
            if (value.getIdTask().equals(idTask)) {
                task = value;
                break;
            }
        }
        return task;
    }

    /**
     * 使用idProject查找子任务
     *
     * @param idProject project id
     * @return 返回相应的主任务列表
     */
    public static List<Task> getTaskListByIdProject(String idProject) {
        List<Task> taskList = new ArrayList<>();
        for (Task value : getTaskList()) {
            if (value.getIdProject().equals(idProject) && value.getIdParentTask().equals("NULL")) {
                taskList.add(value);
            }
        }
        return taskList;
    }

    /**
     * 使用idParentTask查找子任务
     *
     * @param idParentTask parent task id
     * @return 返回相应的子任务列表
     */
    public static List<Task> getTaskListByIdParentTask(String idParentTask) {
        List<Task> taskList = new ArrayList<>();
        for (Task value : getTaskList()) {
            if (value.getIdParentTask().equals(idParentTask)) {
                taskList.add(value);
            }
        }
        return taskList;
    }

    /**
     * 使用idTask删除任务
     *
     * @param idTask task id
     */
    public static void deleteTaskByIdTask(String idTask) {
        List<Task> taskList = getTaskList();
        for (Task value : taskList) {
            if (value.getIdTask().equals(idTask)) {
                UserTaskUtils.deleteUserTaskByIdTask(value.getIdTask());
                taskList.remove(value);
                break;
            }
        }
        writeTaskXML(taskList);
    }

    /**
     * 使用idProject删除任务
     *
     * @param idProject project id
     */
    public static void deleteTaskByIdProject(String idProject) {
        List<Task> taskList = getTaskList();
        for (Task value : taskList) {
            if (value.getIdProject().equals(idProject)) {
                UserTaskUtils.deleteUserTaskByIdTask(value.getIdTask());
                taskList.remove(value);
            }
        }
        writeTaskXML(taskList);
    }

}
