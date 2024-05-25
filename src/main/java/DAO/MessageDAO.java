package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.*;
import classes.*;
public class MessageDAO {
    private static Connection connection;

    public static void setConnection(Connection connection) {
        MessageDAO.connection = connection;
    }

    public static Message getMessageById(int id) throws SQLException {
        Message message = null;
        String sql = "SELECT * FROM messages WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                message = Message.setSelfFromResult(rs);
            }

        }

        return message;
    }


    public static void addMessage(Message message) throws SQLException {
        String sql = "INSERT INTO messages " +
                " (content , message_type , sending_object_id , other_object_id) " +
                " VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            message.toSqlStatement(statement);
            statement.executeUpdate();

        }
    }

    public static void updateMessage(Message message) throws SQLException {
        String sql = "UPDATE messages " +
                "SET  content=? , message_type=? , sending_object_id=? , other_object_id=?"
                +" WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            message.toSqlStatement(statement);
            statement.setInt(8, message.getId());
            statement.executeUpdate();
        }
    }

    public static void deleteMessage(int id) throws SQLException {
        String sql = "DELETE FROM messages WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }
    public static void deleteLink(int id,int id2,String ss) throws SQLException {
        String sql = "DELETE FROM messages WHERE sending_object_id=? AND other_object_id = ? AND message_type= ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setInt(2, id2);
            statement.setString(3, ss);
            statement.executeUpdate();
        }
    }


    public static List<Message> getMessagesByMessage(Message message) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * " +
                " FROM messages" +
                " WHERE other_object_id = ? AND message_type='reply'";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, message.getId());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Message sub_message = Message.setSelfFromResult(rs);
            messages.add(sub_message);
        }

        return messages;
    }

    public static List<Message> getMessagesByMessageId(int id) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * " +
                " FROM messages" +
                " WHERE other_object_id = ? AND message_type='reply'";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Message sub_message = Message.setSelfFromResult(rs);
            messages.add(sub_message);
        }

        return messages;
    }

    /*CREATE TABLE messages (
    id INT PRIMARY KEY,

    content VARCHAR2(1024),
    message_type VARCHAR2(255) NOT NULL,

    account_id INT,
    other_object_id INT,

    FOREIGN KEY (account_id) REFERENCES accounts(id)
);
*/

    public static List<Message> getAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM messages";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            messages.add(Message.setSelfFromResult(rs));
        }

        return messages;
    }

}

