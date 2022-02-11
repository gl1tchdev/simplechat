package chat.com.UI;


import java.io.*;
import java.net.ConnectException;
import java.net.Socket;


public class ClientConnection{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private messageListener messageListener;
//    private messageSender messageSender;

    public ClientConnection(String address, int PORT) {
        try {
            try {
                socket = new Socket(address, PORT);
            }
            catch (ConnectException c) {
                Client.getGui().showError("Сервер недоступен");
                Client.getGui().close();
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            messageListener = new messageListener();
            Client.getGui().addMessage("Send nickname to start chatting!");
            messageListener.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Client.getGui().getTextField1().addActionListener(e -> {
            if (e.getActionCommand().isEmpty()) return;
            send(e.getActionCommand());
            Client.getGui().getTextField1().setText("");
        });
    }

    private class messageListener extends Thread {
        private boolean loop = true;

        public void finish() {
            this.loop = false;
        }

        @Override
        public void run() {
            try {
                while (loop) {
                    String message = null;
                    try {
                        message = get();
                    }
                    catch (NullPointerException e) {
                        finish();
                        close();
                        Client.getGui().showError("Disconnect");
                    }
                    assert message != null;
                    if (message.equals("disconnect")){
                        return;
                    }
                    Client.getGui().addMessage(message);
                    Client.getGui().getList1().ensureIndexIsVisible(Client.getGui().getListModel().size()-1);
                }
            } finally {
                close();
            }
        }
    }
    public String get(){
        try {
            return in.readLine();
        } catch (IOException e) {
            close();
            Client.getGui().showError("Связь с сервером потеряна");
            Client.getGui().close();
        }
        return null;
    }

    public void send(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            close();
            Client.getGui().showError("Связь с сервером потеряна");
            Client.getGui().close();
        }
    }

    public void close() {
        try {
            messageListener.finish();
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException ignored) {

        }
    }

}
