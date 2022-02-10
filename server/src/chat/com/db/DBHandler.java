package chat.com.db;

import org.sqlite.JDBC;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHandler {
    private static final String CON_STR = "jdbc:sqlite:" + System.getProperty("user.dir") + "\\server\\database-src\\chat.db";

    private static DBHandler instance = null;

    public static synchronized DBHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DBHandler();
        return instance;
    }

    private final Connection connection;

    private DBHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    public List<Message> getLastMessages() {
        try (Statement statement = this.connection.createStatement()) {
            List<Message> messages = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM messages ORDER BY id ASC LIMIT 10;");
            while (resultSet.next()) {
                Message msg = new Message();
                msg.setNickname(resultSet.getString("nickname"));
                msg.setId(resultSet.getInt("id"));
                msg.setText(resultSet.getString("text"));
                msg.setDate(resultSet.getString("date"));
                messages.add(msg);
            }
            return messages;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public void addMessage(Message message) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO messages(`nickname`, `text`, `date`) " +
                        "VALUES(?, ?, ?)")) {
            statement.setObject(1, message.getNickname());
            statement.setObject(2, message.getText());
            statement.setObject(3, message.getDate());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
