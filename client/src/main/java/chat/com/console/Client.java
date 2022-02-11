package chat.com.console;


import java.util.Scanner;

public class Client {
    private static final Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }

    public static void main(String[] args) {
        System.out.print("Server addr:ip :");
        String in = scanner.nextLine();
        String[] result = in.split(":");
        new ClientConnection(result[0], Integer.parseInt(result[1]));
    }
}
