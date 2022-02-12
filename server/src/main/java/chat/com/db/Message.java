package chat.com.db;


import chat.com.ServiceStatusWord;

/**
 * Chat message model
 */
public class Message{
    /**
     * {@value} Primary key of db. Not displayed in chat area
     */
    private int id;
    /**
     * {@value} Message author
     */
    private String nickname;
    /**
     * {@value} Message body
     */
    private String text;
    /**
     * {@value} Date of sending this message
     */
    private String date;


    /**
     * @return Ready message, which could be sent
     */
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
