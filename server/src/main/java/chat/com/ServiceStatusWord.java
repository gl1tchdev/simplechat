package chat.com;

/**
 * This enum created for using pair: WORD - value. When user join or leave #ServerConnection:BroadcastServiceMessage() is run with next
 * arg: ServerStatusWord.JOINED, or ServerStatus.
 */
public enum ServiceStatusWord {
    JOINED("joined"), LEFT("left");
    private final String value;

    ServiceStatusWord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
