package chat.com.UI;

public class Client {

    /**
     * Graphical interface of chat client
     * @see ClientGUI
     */
    private static final ClientGUI gui = ClientGUI.getInstance();

    /**
     * @return GUI
     * @see ClientGUI
     */
    public static ClientGUI getGui() {
        return gui;
    }

    public static void main(String[] args) {
        String in = getGui().askQuestion("Enter address ip:port [by default address=localhost:4004]");
        if (in == null) {
            System.exit(0);
            return;
        }
        if (in.isEmpty()) {
            new ClientConnection("localhost", 4004);
            return;
        }
        String[] result = new String[]{};
        try {
            result = in.split(":");
        }
        catch (Exception ignored){
            getGui().close();
            System.exit(0);
        }
        gui.getTextField1().requestFocusInWindow();
        new ClientConnection(result[0], Integer.parseInt(result[1]));
    }
}
