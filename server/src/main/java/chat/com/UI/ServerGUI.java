package chat.com.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * Graphical wrapper for server
 */
public class ServerGUI {
    private JPanel panel1;
    private JList<String> list1;
    private JScrollPane scroll;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private static ServerGUI instance = null;


    public static synchronized ServerGUI getInstance(){
        if (instance == null)
            instance = new ServerGUI();
        return instance;
    }
    /**
     * @return Dimension, which contains width and height of screen user device
     */
    private Dimension getSize(){
        return Toolkit. getDefaultToolkit(). getScreenSize();
    }


    public ServerGUI() {
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        JFrame frame = new JFrame("ServerGUI");
        frame.setContentPane(panel1);
        list1.setModel(listModel);
        Dimension dim = getSize();
        frame.setTitle("Server");
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

    public void addMessage(String st){
        listModel.addElement(st);
    }
}
