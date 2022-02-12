package chat.com.console;

import chat.com.db.DBHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 4004;
    private static ServerSocket server;
    /**
     * {@value} Contains in pair Nickname => ServerConnection
     */
    private static ConcurrentHashMap<String, ServerConnection> serverList = new ConcurrentHashMap<>();
    /**
     * @see DBHandler
     */
    private static DBHandler dbHandler;

    public static DBHandler getDbHandler() {
        return dbHandler;
    }

    public static ConcurrentHashMap<String, ServerConnection> getServerList() {
        return serverList;
    }

    public static void setServerList(ConcurrentHashMap<String, ServerConnection> serverList) {
        Server.serverList = serverList;
    }

    /**
     * Endlessly accept new connections and puts they to serverList
     */
    private static class connectionListener extends Thread {
        private static boolean loop = true;

        public static void finish (){
            loop = false;
        }
        @Override
        public void run() {
            try {
                while (loop){
                    Socket socket = server.accept();
                    ServerConnection conn = new ServerConnection(socket);
                    serverList.put(conn.getNickname(), conn);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            downService();
        }
    }


    /**
     * Shut down the server
     */
    public static void downService(){
        serverList.forEach((nickname,conn) -> conn.close());
    }

    /**
     * @return Server ip
     */
    public static String getOwnIp(){
        String realip = null;
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            assert whatismyip != null;
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert in != null;
            realip = in.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return realip;
    }

    public static void main(String[] args) {
        try {
            dbHandler = DBHandler.getInstance();
            server = new ServerSocket(PORT);
            System.out.println(getOwnIp() + ":" + PORT);
            connectionListener connectionListener = new connectionListener();
            System.out.println("Waiting for connections...");
            connectionListener.start();
        } catch (IOException | SQLException e) {
            connectionListener.finish();
            try {
                server.close();
            } catch (IOException ignored) {
            }
        }
    }
}
