package controller.utlis;

import model.user.User;
import model.user.UserReadWrite;

import java.util.List;

public class UserUtils {

    public static List<User> getUserList() {
        return UserReadWrite.readUserXML();
    }

    public static void writeUserXML(List<User> userList) {
        new UserReadWrite().writeXML(userList);
    }

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

    public static boolean ifSameUsername(String username) {
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

    public static boolean ifPasswordValid(String password) {
        boolean flag = true;
        boolean numberFlag = false;
        boolean letterFlag = false;

        for (int i = 0; i < password.length(); i++) {
            char temp = password.charAt(i);
            if (Character.isDigit(temp)) {
                numberFlag = true;
            } else if (Character.isLetter(temp)) {
                letterFlag = true;
            } else {
                flag = false;
                break;
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

    public static String signUpSubmit(String username, String password, String question, String answer) {
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
        return id;
    }

    public static void updateUserList(User user) {
        List<User> userList = getUserList();
        userList.removeIf(temp -> temp.getIdUser().equals(user.getIdUser()));
        userList.add(user);
        writeUserXML(userList);
    }

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

    public static User searchUserByID(String id) {
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

    public static User searchUserByUsername(String username){
        String id = UserUtils.getIDByUsername(username);
        return UserUtils.searchUserByID(id);
    }


}
