package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.*;
import classes.*;
public class MediaDAO {
    private static Connection connection;

    public static  void setConnection(Connection connection) {
        MediaDAO.connection = connection;
    }

    public static MediaObj getMediaById(int id) throws SQLException {
        MediaObj mediaObj = null;
        String sql = "SELECT * FROM media WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                mediaObj = MediaObj.setSelfFromResult(rs);
            }

        }

        return mediaObj;
    }
    public static MediaObj getMediaByName(String name) throws SQLException {
        MediaObj mediaObj = null;
        if(name==null){
            return null;
        }

        String sql = "SELECT * FROM media WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                mediaObj = MediaObj.setSelfFromResult(rs);
            }

        }

        return mediaObj;
    }

    public static void addMedia(MediaObj mediaObj) throws SQLException {
        String sql = "INSERT INTO media " +
                "(type, parent_id, name, image_url, video_url, season_number, start_date, language, country, genre, description) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            mediaObj.toSqlStatement(statement);
            statement.executeUpdate();

        }
    }

    public static void updateMedia(MediaObj mediaObj) throws SQLException {
        String sql = "UPDATE media " +
                "SET type = ?, parent_id = ?, name = ?, image_url = ?, video_url = ?, season_number = ?, start_date = ?, language = ?, country = ?, genre = ?, description = ?"
                +" WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            mediaObj.toSqlStatement(statement);
            statement.setInt(12, mediaObj.getId());
            statement.executeUpdate();
        }
    }

    public static void deleteMedia(int id) throws SQLException {
        String sql = "DELETE FROM media WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }
    public static List<MediaObj> getMediasByAccount(Account account, String mediaType) throws SQLException {
        List<MediaObj> mediaObjs = new ArrayList<>();
        String query = "SELECT m.id, m.type, m.parent_id, m.name, m.image_url, m.video_url, m.season_number, m.start_date, m.language, m.country, m.genre, m.description " +
                " FROM media m " +
                " JOIN Media_Account_relations mar ON m.id = mar.media_id " +
                " WHERE mar.account_id = ? AND m.type = ?";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, account.getId());
        statement.setString(2, mediaType);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            MediaObj mediaObj = MediaObj.setSelfFromResult(rs);
            mediaObjs.add(mediaObj);
        }

        return mediaObjs;
    }

    public static List<MediaObj> getMediasByMedia(MediaObj mediaObj) throws SQLException {
        List<MediaObj> mediaObjs = new ArrayList<>();
        String query = "SELECT * " +
                " FROM media" +
                " WHERE parent_id = ?";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, mediaObj.getId());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            MediaObj sub_mediaObj = MediaObj.setSelfFromResult(rs);
            mediaObjs.add(sub_mediaObj);
        }

        return mediaObjs;
    }

    /*CREATE TABLE messages (
    id INT PRIMARY KEY,

    content VARCHAR2(1024),
    message_type VARCHAR2(255) NOT NULL,

    sending_object_id INT,
    other_object_id INT,

);
*/
    public static List<Message> getCommentsByMedia(MediaObj mediaObj) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * " +
                "FROM messages" +
                " WHERE other_object_id = ? AND message_type= 'comment'";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, mediaObj.getId());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Message message = Message.setSelfFromResult(rs);
            messages.add(message);
        }

        return messages;
    }

    public static List<MediaObj> getAllMedia() throws SQLException {
        List<MediaObj> mediaObjs = new ArrayList<>();
        String query = "SELECT * FROM media";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            MediaObj mediaObj = MediaObj.setSelfFromResult(rs);
            mediaObjs.add(mediaObj);
        }

        return mediaObjs;
    }

}
