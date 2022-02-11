package model.user;

public class User {
    private String idUser;
    private String username;
    private String password;
    private String question;
    private String answer;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toString()
    {

        return "----------------------------\n" +
                "idUser: " + idUser + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n" +
                "Q: " + question + "\n" +
                "A: " + answer + "\n" +
                "----------------------------\n";

    }
}

