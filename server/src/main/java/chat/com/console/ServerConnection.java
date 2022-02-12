package chat.com.console;


import chat.com.ServiceStatusWord;
import chat.com.db.Message;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnection{
    private String nickname;
    private final Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private MessageBroadcaster messageBroadcaster;

    public ServerConnection(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.nickname = get();
            messageBroadcaster = new MessageBroadcaster();
            messageBroadcaster.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Message> messages = Server.getDbHandler().getLastMessages();
        messages.forEach(i -> this.send(i.toString()));
    }
    public String getNickname() {
        return nickname;
    }

    public void send(String message){
        if (message.isEmpty()) return;
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            close();
            broadcastServiceMessage(ServiceStatusWord.LEFT);
        }
    }

    public String get() {
        try {
            return in.readLine();
        } catch (IOException e) {
            broadcastServiceMessage(ServiceStatusWord.LEFT);
            close();
        }
        return "";
    }

    /**
     * @return Current time
     */
    public String getTime(){
        Date time = new Date();
        SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dt1.format(time);
    }

    /**
     * Gets input message from one client and send it to other
     */
    private class MessageBroadcaster extends Thread{
        private boolean loop = true;

        public void finish() {
            this.loop = false;
        }

        @Override

        public void run() {
            broadcastServiceMessage(ServiceStatusWord.JOINED);
            while (loop){
                String message = get();
                if (message.isEmpty()) continue;

                if (message.equals("disconnect")){
                    send("disconnect");
                    broadcastServiceMessage(ServiceStatusWord.LEFT);
                    return;
                }
                else {
                    broadcastChatMessage(message);
                }
            }
        }
    }

    /**
     * Saves messages in correct format to db
     * @param message Original message
     */
    private void saveMessage(String message){
        Message msg = new Message();
        msg.setDate(getTime());
        msg.setText(message);
        msg.setNickname(getNickname());
        Server.getDbHandler().addMessage(msg);
    }

    /**
     * @param message Send message to all clients
     */
    private void broadcastMessage(String message){
        ConcurrentHashMap<String, ServerConnection> list = Server.getServerList();
        if (list.isEmpty()) return;
        for (ServerConnection conn: list.values()) {
            conn.send(message);
        }
        Server.setServerList(list);
        System.out.println(message);
    }

    /**
     * Uses to show default message
     * @param message Converts original message to need format
     */
    private void broadcastChatMessage(String message){
        saveMessage(message);
        broadcastMessage("(" + getTime() + ") " + getNickname() + ": " + message);
    }

    /**
     * Uses to show event with user
     * @param action Action is status word
     */
    private void broadcastServiceMessage(ServiceStatusWord action){
        saveMessage(action.getValue());
        broadcastMessage("(" + getTime() + ") " + getNickname() + " " + action.getValue());
    }

    /**
     * Stops messageBroadcaster, removes current connection from serverlist, closes socket streams
     */
    public void close(){
        try {
            messageBroadcaster.finish();
            ConcurrentHashMap<String, ServerConnection> list = Server.getServerList();
            list.remove(getNickname());
            Server.setServerList(list);
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException ignored) {

        }
    }
}
