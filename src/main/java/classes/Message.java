package classes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import service.Controller_util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Message   {
    //SELECT id , content , message_type , sending_object_id , other_object_id
    private int id;
    private String content;
    private String message_type;
    private int sending_object_id;
    private int other_object_id;
    private Date sentDate;

    public Message(int id , String content , String message_type , int sending_object_id , int other_object_id/*, Date sentDate*/) {
        this.id = id;
        this.message_type = message_type;
        this.sending_object_id = sending_object_id;
        this.other_object_id= other_object_id;
        this.content = content;
        //this.sentDate = sentDate;
    }

    public int getId() {
        return id;
    }

    public int getSending_object_id() {
        return sending_object_id;
    }

    public int getOther_object_id() {
        return other_object_id;
    }

    public String getContent() {
        return content;
    }

    public Date getSentDate() {
        return sentDate;
    }

    //SELECT id , content , message_type , account_id , sending_object_id
    public void toSqlStatement(PreparedStatement statement) throws SQLException {
        //statement.setInt(1, id);
        statement.setString(1, content);
        statement.setString(2, message_type);
        statement.setInt(3, sending_object_id);
        statement.setInt(4, other_object_id);
        /*statement.setTimestamp(6, new java.sql.Timestamp(sentDate.getTime()));*/
    }

    public static Message setSelfFromResult(ResultSet rs) throws SQLException {
        return new Message(
        rs.getInt("id"),
        rs.getString("content"),
        rs.getString("message_type"),
        rs.getInt("sending_object_id"),
        rs.getInt("other_object_id")/*,
        rs.getTimestamp("sent_date")*/
        );
    }

    public String toString(){
        return "{"+id +","+ content+","+ message_type+","+ sending_object_id+","+ other_object_id+"}";
    }
}

