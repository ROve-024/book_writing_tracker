package utils;

import io.task.Task;
import io.user.User;
import io.user.UserReadWrite;
import io.userproject.UserProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 处理User类对象的数据
 *
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class UserUtils {
    /**
     * 从文件中获取UserList
     *
     * @return 储存的用户列表
     */
    public static List<User> getUserList() {
        return UserReadWrite.readUserXML();
    }

    /**
     * 将用户列表保存到文件
     *
     * @param userList 要写入文件的用户列表
     */
    public static void writeUserXML(List<User> userList) {
        new UserReadWrite().writeUserXML(userList);
    }

    /**
     * 获取最大的用户id值
     *
     * @param userList 已有的用户列表
     * @return 返回即最大id
     */
    public static int maxUserId(List<User> userList) {
        int cnt = 0;
        User user;
        for (User value : userList) {
            user = value;
            int temp = Integer.parseInt(user.getIdUser());
            if (cnt <= temp) {
                cnt = temp;
            }
        }
        return cnt;
    }

    /**
     * 验证用户输入的账号和密码是否和本地数据一致
     *
     * @param username 用户输入的用户名
     * @param password 用户输入的密码
     * @return 若匹配，则返回true，反之亦然
     */
    public static boolean loginMatch(String username, String password) {
        List<User> userList = getUserList();
        boolean flag = false;
        User user;

        for (User value : userList) {
            user = value;
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 检测用户输入的用户名是否已经存在
     *
     * @param username 用户输入的用户名
     * @return 若存在，则返回true，反之亦然
     */
    public static boolean isSameUsername(String username) {
        List<User> userList = getUserList();
        boolean flag = false;

        for (User temp : userList) {
            if (temp.getUsername().equals(username)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    /**
     * 判断用户输入的密码是否符合要求
     *
     * @param password 用户输入的密码
     * @return 若符合要求，则返回false，反之亦然
     */
    public static boolean isPasswordValid(String password) {
        boolean flag = true;
        boolean numberFlag = false;
        boolean letterFlag = false;

        for (int i = 0; i < password.length(); i++) {
            char temp = password.charAt(i);
            if (Character.isDigit(temp)) {
                numberFlag = true;
            } else if (Character.isLetter(temp)) {
                letterFlag = true;
            }
        }
        if (!(letterFlag && numberFlag)) {
            flag = false;
        }

        if (password.length() < 6 || password.length() > 16) {
            flag = false;
        }

        return !flag;
    }

    /**
     * 判断用户输入的邮箱是否符合要求
     *
     * @param email 用户输入的邮箱
     * @return 若符合要求，则返回false，反之亦然
     */
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
                "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]" +
                "|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 将用户输入的信息注册成一条新的用户数据，并储存到本地
     *
     * @param username 用户输入的用户名
     * @param password 用户输入的密码
     * @param question 用户选择的密保问题
     * @param answer   用户输入的密保问题答案
     */
    public static void signUpSubmit(String username, String password, String question, String answer) {
        List<User> userList = getUserList();
        User user = new User();
        String id = Integer.toString(maxUserId(userList) + 1);
        user.setIdUser(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setQuestion(question);
        user.setAnswer(answer);
        userList.add(user);
        writeUserXML(userList);
    }

    /**
     * 更新用户数据，并更新相应文件
     *
     * @param user 需要更新的用户
     */
    public static void updateUserList(User user) {
        List<User> userList = getUserList();
        userList.removeIf(temp -> temp.getIdUser().equals(user.getIdUser()));
        userList.add(user);
        writeUserXML(userList);
    }

    /**
     * 使用用户名获取用户id
     *
     * @param username 用户名
     * @return 返回相应用户的用户id
     */
    public static String getIDByUsername(String username) {
        List<User> userList = getUserList();
        User user = null;

        for (User value : userList) {
            User temp;
            temp = value;
            if (temp.getUsername().equals(username)) {
                user = temp;
            }
        }
        assert user != null;
        return user.getIdUser();
    }

    /**
     * 通过用户id获取User对象
     *
     * @param id 用户id
     * @return 返回相应的User对象
     */
    public static User getUserByID(String id) {
        List<User> userList = getUserList();
        User user = null;

        for (User value : userList) {
            User temp;
            temp = value;
            if (temp.getIdUser().equals(id)) {
                user = temp;
            }
        }
        return user;
    }

    /**
     * 通过用户名获取User对象
     *
     * @param username 用户名
     * @return 返回相应的User对象
     */
    public static User getUserByUsername(String username) {
        String id = UserUtils.getIDByUsername(username);
        return UserUtils.getUserByID(id);
    }

    /**
     * 获取子任务可选用户列表
     *
     * @param searchInput 用户输入的搜索关键词
     * @return 返回子任务可选用户列表
     */
    public static List<User> getSubtaskSelectUserList(String searchInput) {
        List<User> userList = getUserListByIdProject();
        String taskType = JsonUtils.getBuffer().getString("typeSubtask");

        String otherExecutor = "NULL";
        try {
            if (taskType.equals("writing task")) {
                otherExecutor = getUserByID(TaskUtils.getProofreadSubtask().getIdUser()).getUsername();
            } else if (taskType.equals("proofreading task")) {
                otherExecutor = getUserByID(TaskUtils.getWritingSubtask().getIdUser()).getUsername();
            }
        } catch (NullPointerException ignored) {
            otherExecutor = "NULL";
        }

        if (!otherExecutor.equals("NULL")) {
            String finalOtherExecutor = otherExecutor;
            userList.removeIf(user -> user.getUsername().equals(finalOtherExecutor));
        }

        if (!searchInput.equals("")) {
            userList.removeIf(user -> !user.getUsername().toLowerCase().contains(searchInput.toLowerCase()));
        }
        return userList;
    }

    /**
     * 查询参与该项目的所有用户
     *
     * @return 返回属于参与项目的所有用户列表
     */
    public static List<User> getUserListByIdProject() {
        String idProject = JsonUtils.getBuffer().getString("idProject");

        List<User> result = new ArrayList<>();
        List<UserProject> userProjectList = UserProjectUtils.getUserProjectList();

        for (UserProject value : userProjectList) {
            if (value.getIdProject().equals(idProject)) {
                result.add(getUserByID(value.getIdUser()));
            }
        }
        return result;
    }


}
