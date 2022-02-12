package model.user;

/**
 * User数据结构
 *
 * @author CUI, Bingzhe
 * @version 1.0
 */
public class User {
    private String idUser;
    private String username;
    private String password;
    private String question;
    private String answer;
    private String email;
    private String telephone;


    /**
     * 初始化用户数据
     */
    public User() {
        idUser = "NULL";
        username = "NULL";
        password = "NULL";
        question = "NULL";
        answer = "NULL";
        email = "NULL";
        telephone = "NULL";
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
     * Gets username
     *
     * @return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username
     *
     * @param username set value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password
     *
     * @return value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password
     *
     * @param password set value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets question
     *
     * @return value of question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set question
     *
     * @param question set value of question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets answer
     *
     * @return value of answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Set answer
     *
     * @param answer set value of answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Gets email
     *
     * @return value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email
     *
     * @param email set value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets telephone
     *
     * @return value of telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Set telephone
     *
     * @param telephone set value of telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    /**
     * @return 将User对象用字符串表示
     */
    public String toString() {

        return "----------------------------\n" +
                "idUser: " + idUser + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n" +
                "Q: " + question + "\n" +
                "A: " + answer + "\n" +
                "email" + email + "\n" +
                "telephone" + telephone + "\n" +
                "----------------------------\n";

    }
}

