package chat.com.console;


import java.io.*;
import java.net.ConnectException;
import java.net.Socket;


/**
 * Class of client connection to server
 */
public class ClientConnection{
    /**
     * {@value} Client socket
     */
    private Socket socket;
    /**
     * {@value} Buffered reader of input socket stream
     */
    private BufferedReader in;
    /**
     * {@value} Buffered writer of output socket stream
     */
    private BufferedWriter out;
    /**
     * {@value} Field for message listener
     * @see messageListener
     */
    private messageListener messageListener;
    /**
     * {@value} Field for message sender
     * @see messageSender
     */
    private messageSender messageSender;


    /**
     * Creates socket, input and output streams
     * @param address IP-address of server
     * @param PORT Port of server
     */
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

    /**
     * Endlessly gets messages from server and sends to output stream
     *
     */
    private class messageListener extends Thread {
        /**
         * {@value} Uses to control run()
         */
        private boolean loop = true;

        /**
         * Stops the messageListener
         */
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
    /**
     * Endlessly gets messages from input stream and sends to server
     */
    private class messageSender extends Thread{
        /**
         * {@value} Uses to control messageSender#run()
         */
        private boolean loop = true;
        /**
         * Stops the messageSender
         */
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

    /**
     * Gets string from input socket stream
     * @return message from server
     */
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

    /**
     * Put string to output socket stream
     * @param message message which sends to server
     */
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

    /**
     * Closes socket, listeners, and it's streams
     */
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
