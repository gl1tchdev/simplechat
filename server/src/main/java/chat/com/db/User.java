package chat.com.db;

public class User {
    private String nickname;
    private String RegistrationDate;

    public User(String nickname, String date) {
        this.nickname = nickname;
        this.RegistrationDate = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public void setDate(String date) {
        this.RegistrationDate = date;
    }
}
