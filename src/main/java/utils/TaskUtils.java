package utils;

import com.alibaba.fastjson.JSONObject;
import io.task.Task;
import io.task.TaskReadWrite;
import io.user.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
     * 获取下一个任务id
     *
     * @param taskList 已有的任务列表
     * @return 返回下一个任务id
     */
    public static int getNextTaskId(List<Task> taskList) {
        int cnt = 0;
        Task task;
        for (Task value : taskList) {
            task = value;
            int temp = Integer.parseInt(task.getIdTask());
            if (cnt <= temp) {
                cnt = temp;
            }
        }
        return cnt + 1;
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
     * 使用idProject查找已完成的主任务
     *
     * @param idProject project id
     * @return 返回相应的主任务列表
     */
    public static List<Task> getFinishedTaskListByIdProject(String idProject) {
        List<Task> taskList = new ArrayList<>();
        for (Task value : getTaskList()) {
            if (value.getIdProject().equals(idProject) && value.getIdParentTask().equals("NULL") && value.getStatus().equals("finished")) {
                taskList.add(value);
            }
        }
        return sortTaskByDefaultOrder(taskList);
    }

    /**
     * 使用idProject查找未完成的主任务
     *
     * @param idProject project id
     * @return 返回相应的主任务列表
     */
    public static List<Task> getUnfinishedTaskListByIdProject(String idProject) {
        List<Task> taskList = new ArrayList<>();
        for (Task value : getTaskList()) {
            if (value.getIdProject().equals(idProject) && value.getIdParentTask().equals("NULL") && value.getStatus().equals("unfinished")) {
                taskList.add(value);
            }
        }
        return sortTaskByDefaultOrder(taskList);
    }

    /**
     * 使用idParentTask查找子任务
     *
     * @param idParentTask parent task id
     * @return 返回相应的子任务列表
     */
    public static List<Task> getSubtaskListByIdParentTask(String idParentTask) {
        List<Task> taskList = new ArrayList<>();
        for (Task value : getTaskList()) {
            if (value.getIdParentTask().equals(idParentTask)) {
                taskList.add(value);
            }
        }
        return taskList;
    }

    /**
     * 检测任务执行人/创建人是否包含当前用户
     *
     * @param task 需要进行判断的Task对象
     */
    public static boolean isTaskAndSubtaskContainUser(Task task){
        boolean result = false;
        User user = UserUtils.getUserByUsername((String) JsonUtils.getBuffer().get("username"));
        if(task.getIdUser().equals(user.getIdUser())){
            result =  true;
        } else {
            for(Task subtask : getSubtaskListByIdParentTask(task.getIdTask())){
                if (subtask.getIdUser().equals(user.getIdUser())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取写作子任务
     *
     * @return 返回当前任务的写作子任务
     */
    public static Task getWritingSubtask(){
        String idParentTask = JsonUtils.getBuffer().getString("idTask");
        Task result = null;
        for (Task value : getTaskList()) {
            if (value.getIdParentTask().equals(idParentTask) && value.getDescription().equals("writing task")) {
                result = value;
                break;
            }
        }
        return result;
    }

    /**
     * 获取校对子任务
     *
     * @return 返回当前任务的校对子任务
     */
    public static Task getProofreadSubtask(){
        String idParentTask = JsonUtils.getBuffer().getString("idTask");
        Task result = null;
        for (Task value : getTaskList()) {
            if (value.getIdParentTask().equals(idParentTask) && value.getDescription().equals("proofreading task")) {
                result = value;
                break;
            }
        }
        return result;
    }
    
    /**
     * 使用idTask删除任务
     *
     * @param idTask task id
     */
    public static void deleteTaskByIdTask(String idTask) {
        List<Task> taskList = getTaskList();
        Iterator<Task> iterator = taskList.iterator();
        while(iterator.hasNext()){
            Task task = iterator.next();
            if(task.getIdParentTask().equals(idTask)){
                iterator.remove();
            }
            else if(task.getIdTask().equals(idTask)){
                iterator.remove();
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
        Iterator<Task> iterator = taskList.iterator();
        while(iterator.hasNext()){
            Task task = iterator.next();
            if(task.getIdProject().equals(idProject)){
                iterator.remove();
            }

        }
        writeTaskXML(taskList);
    }

    /**
     * 使用默认方式，有截止日期排在前，无截止日期排在后
     *
     * @param taskList 待排序的任务列表
     */
    public static List<Task> sortTaskByDefaultOrder(List<Task> taskList){
        List<Task> result = new ArrayList<>();
        List<Task> withDeadlineList = new ArrayList<>();
        List<Task> withoutDeadlineList = new ArrayList<>();
        for(Task task : taskList){
            if(task.getDeadlineTime() == -1){
                withoutDeadlineList.add(task);
            }else {
                withDeadlineList.add(task);
            }
        }
        sortTaskByDeadlineOrder(withDeadlineList);
        sortTaskByCreateReversedOrder(withoutDeadlineList);
        result.addAll(withDeadlineList);
        result.addAll(withoutDeadlineList);
        return result;
    }

    /**
     * 使用截止时间顺序
     *
     * @param taskList 待排序的任务列表
     */
    public static List<Task> sortTaskByDeadlineOrder(List<Task> taskList) {
        taskList.sort(Comparator.comparing(Task::getDeadlineTime));
        return taskList;
    }

    /**
     * 使用截止时间逆序
     *
     * @param taskList 待排序的任务列表
     */
    public static List<Task> sortTaskByDeadlineReversedOrder(List<Task> taskList) {
        taskList.sort(Comparator.comparing(Task::getDeadlineTime).reversed());
        return taskList;
    }

    /**
     * 使用创建时间顺序
     *
     * @param taskList 待排序的任务列表
     */
    public static List<Task> sortTaskByCreateOrder(List<Task> taskList) {
        taskList.sort(Comparator.comparing(Task::getCreateTime));
        return taskList;
    }

    /**
     * 使用创建时间逆序
     *
     * @param taskList 待排序的任务列表
     */
    public static List<Task> sortTaskByCreateReversedOrder(List<Task> taskList) {
        taskList.sort(Comparator.comparing(Task::getCreateTime).reversed());
        return taskList;
    }

    /**
     * 使用idProject查询任务总数
     *
     * @param idProject project id
     */
    public static int getAllTaskNumberByIdProject(String idProject) {
        return getFinishedTaskListByIdProject(idProject).size() + getUnfinishedTaskListByIdProject(idProject).size();
    }

    /**
     * 使用idProject查询已完成任务总数
     *
     * @param idProject project id
     */
    public static int getFinishedTaskNumberByIdProject(String idProject) {
        return getFinishedTaskListByIdProject(idProject).size();
    }
    
    public static String getStatusOfUserInProjectByIdUser(String idUser){
        JSONObject buffer = JsonUtils.getBuffer();
        List<Task> finishedTaskList = new ArrayList<>();
        for (Task value : getTaskList()) {
            if (value.getIdProject().equals(buffer.getString("idProject")) && !value.getIdParentTask().equals("NULL") && value.getStatus().equals(
                    "finished") && value.getIdUser().equals(idUser)) {
                finishedTaskList.add(value);
            }
        }

        List<Task> unfinishedTaskList = new ArrayList<>();
        for (Task value : getTaskList()) {
            if (value.getIdProject().equals(buffer.getString("idProject")) && !value.getIdParentTask().equals("NULL") && value.getStatus().equals(
                    "unfinished") && value.getIdUser().equals(idUser)) {
                unfinishedTaskList.add(value);
            }
        }

        return "Finished-Total: (" + finishedTaskList.size() + "/" + (finishedTaskList.size() + unfinishedTaskList.size()) + ")";
    }

}
