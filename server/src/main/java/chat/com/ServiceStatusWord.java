package chat.com;

/**
 * This enum created for using pair: WORD - value
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
