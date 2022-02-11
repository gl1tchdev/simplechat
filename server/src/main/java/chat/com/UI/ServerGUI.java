package chat.com.UI;

import javax.swing.*;

public class ServerGUI {
    private JPanel panel1;
    private JList<String> list1;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    private static ServerGUI instance = null;

    public static synchronized ServerGUI getInstance(){
        if (instance == null)
            instance = new ServerGUI();
        return instance;
    }

    public ServerGUI() {
        JFrame frame = new JFrame("ServerGUI");
        frame.setContentPane(panel1);
        list1.setModel(listModel);
        frame.pack();
        frame.setVisible(true);
    }

    public void addMessage(int id,String st){
        listModel.add(id, st);
    }
}
