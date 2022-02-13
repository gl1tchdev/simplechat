package chat.com.db;

import org.sqlite.JDBC;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Class created for easy and fast managing db
 */
public class DBHandler {
    /**
     * {@value} Path to connect existing db
     */
    private static final String CON_STR = "jdbc:sqlite:" + System.getProperty("user.dir") + "\\server\\database-src\\chat.db";

    /**
     * {@value} Single copy of DBHandler
     */
    private static DBHandler instance = null;

    /**
     *
     * @return DBBHandler instance
     * @throws SQLException Can throw SQLException
     */
    public static synchronized DBHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DBHandler();
        return instance;
    }

    private final Connection connection;


    /**
     * Creates db connect
     * @throws SQLException Can throw SQLException
     */
    private DBHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    /**
     * @return 10 last messages
     */
    public List<Message> getLastMessages() {
        try (Statement statement = this.connection.createStatement()) {
            List<Message> messages = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM (" +
                    "   SELECT * FROM messages ORDER BY id DESC LIMIT 10" +
                    ")Var1" +
                    " ORDER BY id ASC;");
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

    /**
     * Add nickname of joined user to table `users`
     * @param user User object
     */
    public void addToUsers(User user){
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT OR IGNORE INTO users(`nickname`, `date`) " +
                        "VALUES(?, ?)")) {
            statement.setObject(1, user.getNickname());
            statement.setObject(2, user.getRegistrationDate());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Puts chat message to db
     * @param message Message, which is written to db
     */
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
