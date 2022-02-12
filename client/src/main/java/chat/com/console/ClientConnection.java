package chat.com.console;


import java.io.*;
import java.net.ConnectException;
import java.net.Socket;


public class ClientConnection{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private messageListener messageListener;
    private messageSender messageSender;

    public ClientConnection(String address, int PORT) {
        try {
            try {
                socket = new Socket(address, PORT);
            }
            catch (ConnectException c) {
                System.out.println("Server is unavailable");
                System.exit(0);
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            messageListener = new messageListener();
            messageSender = new messageSender();
            messageSender.start();
            messageListener.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        System.out.println("Disconnected");
                    }
                    assert message != null;
                    if (message.equals("disconnect")){
                        return;
                    }
                    System.out.println(message);
                }
            } finally {
                close();
            }
        }
    }

    private class messageSender extends Thread{
        private boolean loop = true;
        public void finish(){
            this.loop = false;
        }

        @Override
        public void run() {
            String nickname = null;
            try {
                while (loop){
                    if (nickname == null) {
                        System.out.print("Enter nickname: ");
                    }
                    String word = Client.getScanner().nextLine();
                    if (nickname == null) {
                        nickname = word;
                    }
                    send(word);
                    if (word.equals("disconnect")) {
                        return;
                    }
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
            System.out.println("Connection lost");
            System.exit(0);
        }
        return null;
    }

    public void send(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            close();
            System.out.println("Connection lost");
            System.exit(0);
        }
    }

    public void close() {
        try {
            messageListener.finish();
            messageSender.finish();
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException ignored) {

        }
    }

}
