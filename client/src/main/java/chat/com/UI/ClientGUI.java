package chat.com.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;


public class ClientGUI {
    private JPanel panel1;
    private JTextField textField1;
    private JList<String> list1;
    private JScrollPane scroll;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JFrame frame = new JFrame("ServerGUI");
    private static ClientGUI instance = null;


    public static synchronized ClientGUI getInstance(){
        if (instance == null)
            instance = new ClientGUI();
        return instance;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    private Dimension getSize(){
        return Toolkit. getDefaultToolkit(). getScreenSize();
    }

    public JList<String> getList1() {
        return list1;
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public ClientGUI() {
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        frame.setContentPane(panel1);
        list1.setModel(listModel);
        Dimension dim = getSize();
        frame.setTitle("Client chat");
        frame.setAlwaysOnTop(true);
        Dimension preferred = new Dimension();
        preferred.setSize(500, 300);
        frame.setLocation((int)Math.round(dim.getWidth()*(1.5/4)),(int)Math.round(dim.getHeight()*(1.1/3)));
        frame.setMinimumSize(preferred);
        frame.setResizable(false);
        list1.setAutoscrolls(true);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                e.getWindow().dispose();
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    public String askQuestion(String question) {
        return JOptionPane.showInputDialog(frame,
                        question, null);
    }

    public void showError(String text) {
        JOptionPane.showMessageDialog(frame, text, "Error",
                JOptionPane.ERROR_MESSAGE);
    }


    public void close(){
        this.frame.dispose();
    }
    public void addMessage(String st){
        listModel.addElement(st);
    }
}
