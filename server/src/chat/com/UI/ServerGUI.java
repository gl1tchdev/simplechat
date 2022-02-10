package chat.com.UI;

import javax.swing.*;

public class ServerGUI {
    private JPanel panel1;
    private JList list1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ServerGUI");
        frame.setContentPane(new ServerGUI().panel1);
        frame.pack();
        frame.setVisible(true);
    }
}
