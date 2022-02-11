package chat.com;

public enum ServiceStatusWord {
    JOINED("подключился"), LEFT("вышел");
    private final String value;

    ServiceStatusWord(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
