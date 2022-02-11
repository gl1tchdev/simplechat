package chat.com.db;


import chat.com.ServiceStatusWord;

public class Message{
    private int id;
    private String nickname;
    private String text;
    private String date;


    @Override
    public String toString() {
        for (ServiceStatusWord word: ServiceStatusWord.values()){
            if (this.text.contains(word.getValue())){
                return String.format("(%s) %s %s", this.date, this.nickname, this.text);
            }
        }
        return String.format("(%s) %s: %s", this.date, this.nickname, this.text);
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }
}
