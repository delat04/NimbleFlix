package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.DB;
import classes.Account;
import classes.MediaObj;
import classes.Message;

public class AccountDAO {
    private static Connection connection;

    public static void setConnection(Connection connection) {
        AccountDAO.connection = connection;
    }

    public static Account getAccountById(int id) throws SQLException {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                account = Account.setSelfFromResult(rs);
            }

        }

        return account;
    }

    public static Account getAccountByUsername(String user) throws SQLException {
        Account account = null;
        String sql = " SELECT * FROM accounts WHERE username = ? ";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                account = Account.setSelfFromResult(rs);
            }

        }

        return account;
    }

    public static void addAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts " +
                "(username,password, type, full_name, profile_pic, email, date_of_birth, about_me) " +
                "VALUES ( ?, ?,?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            account.toSqlStatement(statement);
            statement.executeUpdate();
        }
    }

    public static void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE accounts " +
                "SET username = ?,password= ?, type = ?, full_name = ?, profile_pic = ?, email = ?, date_of_birth = ?, about_me = ? " +
                "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            account.toSqlStatement(statement);
            statement.setInt(9, account.getId());
            statement.executeUpdate();
        }
    }

    public static void deleteAccount(int id) throws SQLException {
        String sql = "DELETE FROM accounts WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public static List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Account account = Account.setSelfFromResult(rs);
            accounts.add(account);
        }

        return accounts;
    }

    public static List<Account> getAccountsByMedias(MediaObj mediaObj) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT *" +
                " FROM accounts m " +
                "JOIN Media_Account_relations mar ON m.id = mar.account_id " +
                "WHERE mar.media_id = ?";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, mediaObj.getId());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Account account = Account.setSelfFromResult(rs);
            accounts.add(account);
        }

        return accounts;
    }
    //SELECT id , content , message_type , sending_object_id , other_object_id
    public static List<Message> getNotificationsByAccount(Account account) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT id , content , message_type , sending_object_id , other_object_id " +
                "FROM messages" +
                " WHERE other_object_id = ? AND message_type= 'notification'";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, account.getId());
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Message message = Message.setSelfFromResult(rs);
            messages.add(message);
        }

        return messages;
    }

    public static List<Message> getNotificationsByAccountId(int id) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT id , content , message_type , sending_object_id , other_object_id " +
                " FROM messages" +
                " WHERE other_object_id = ? AND message_type= 'notification' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Message message = Message.setSelfFromResult(rs);
            messages.add(message);
        }

        return messages;
    }


    public static List<Account> getProducersByMedia(int id) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * " +
                " JOIN messages me ON m.id = me.sending_object_id "+
                " WHERE other_object_id = ? AND message_type= 'produces serie' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Account account = Account.setSelfFromResult(rs);
            accounts.add(account);
        }

        return accounts;
    }



    public static List<Account> getActorsSubsribedTo(int id) throws SQLException {
        List<Account> actors = new ArrayList<>();
        //String query = "SELECT *" +
        //                " FROM accounts m " +
        //                "JOIN Media_Account_relations mar ON m.id = mar.account_id " +
        //                "WHERE mar.media_id = ?";

        String query = "SELECT *" +
                " FROM accounts m" +
                " JOIN messages me ON m.id = me.other_object_id "+
                " WHERE sending_object_id = ? AND message_type= 'subscribed to actor' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Account actor = Account.setSelfFromResult(rs);
            actors.add(actor);
        }

        return actors;
    }

    public static Boolean didProduceMedia(int idAccount,int idMedia) throws SQLException {

        String query = "SELECT * FROM messages "+
                " WHERE sending_object_id = ? AND other_object_id = ? AND message_type= 'produces serie' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, idAccount);
        statement.setInt(2, idMedia);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            return true;
        }
        return false;
    }
    public static Boolean isSubscribed(int idAccount,int idMedia) throws SQLException {

        String query = "SELECT * FROM messages "+
                " WHERE sending_object_id = ? AND other_object_id = ? AND message_type= 'subscribed to serie' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, idAccount);
        statement.setInt(2, idMedia);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            return true;
        }
        return false;
    }
    public static Boolean isSubscribedActor(int idAccount,int idAccount2) throws SQLException {

        String query = "SELECT * FROM messages "+
                " WHERE sending_object_id = ? AND other_object_id = ? AND message_type= 'subscribed to actor' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, idAccount);
        statement.setInt(2, idAccount2);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            return true;
        }
        return false;
    }
    public static List<MediaObj> getSeriesProduced(int id) throws SQLException {
        List<MediaObj> medias = new ArrayList<>();

        String query = "SELECT *" +
                " FROM media m" +
                " JOIN messages me ON m.id = me.other_object_id "+
                " WHERE sending_object_id = ? AND message_type= 'produces serie' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            MediaObj media = MediaObj.setSelfFromResult(rs);
            medias.add(media);
        }

        return medias;
    }

    public static List<MediaObj> getSeriesSubsribedTo(int id) throws SQLException {
        List<MediaObj> medias = new ArrayList<>();

        String query = "SELECT *" +
                " FROM media m" +
                " JOIN messages me ON m.id = me.other_object_id "+
                " WHERE sending_object_id = ? AND message_type= 'subscribed to serie' ";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            MediaObj media = MediaObj.setSelfFromResult(rs);
            medias.add(media);
        }

        return medias;
    }
}

