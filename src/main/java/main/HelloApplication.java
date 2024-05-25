package main;

import DAO.AccountDAO;
import DAO.MediaDAO;
import DAO.MessageDAO;
import DB.DB;
import classes.Account;
import classes.MediaObj;
import classes.Message;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import javafx.stage.StageStyle;
import service.Controller_util;
public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        //System.out.println(HelloApplication.class.getResource("ShowAccount.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("EditAccount.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 0, 0);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Hello!");

        Connection db = DB.getInstance();
        AccountDAO.setConnection(db);
        MediaDAO.setConnection(db);
        MessageDAO.setConnection(db);

        try {
            System.out.println(AccountDAO.getAllAccounts());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        scene.getStylesheets().add("file:src/main/resources/main/styles.css");
        stage.setScene(scene);
        //stage.setY(0);
        stage.show();
        Controller_util.switchTocontroller(stage,"login",null,null,null);

    }

    public static void main(String[] args) {


       //try{

            //System.out.println(m.getAllMedia());
            //a.addAccount(new Account(0,"@Faron4567","f","User","Faron Leopold","aaa.png","fffleo@gmail.com",
            //        null, "First Account!  i am who i am , but who am i? a great unanswerable question"));
            //a.addAccount(new Account(0,"not tamgggmem","strong pass","Producer","Corporation inc","test.jpg","gg@gmail.com",
             //       null, "First Producer!"));
            //m.addMedia(new MediaObj(0,"Serie",0, "Hackers", "ee3e.jpg", "ee1e.mp3",0,null, "English","USA","Action","Its about Hackers!"));
            //System.out.println(m.getAllMedia());
            //m.addMedia(new MediaObj(0,"Serie",0, "Thiefs", "ee2e.jpg", "e2ee.mp3",0,null, "English","USA","Action","Its about Thiefs!"));
            //a.deleteAccount(2);
            //System.out.println(a.getAllAccounts());
            //Account a1= a.getAccountById(1);
            //a1.setAboutMe("this is edited!");
            //a.updateAccount(a1);
            //SELECT id , content , message_type , sending_object_id , other_object_id
            //System.out.println(me.getAllMessages());
            //me.addMessage(new Message(0,"a new movie has been made!","notification",3,4));
            //me.addMessage(new Message(0,"a new movie has been made!!!!","notification",2,4));
            //me.addMessage(new Message(0,"this is the last  reply to the message!!!!","reply",2,4));
            //System.out.println(me.getMessagesByMessageId(4));
            //System.out.println();
            //System.out.println(a.getNotificationsByAccountId(4));
            //System.out.println(a.getAccountById(2));

        //}

        //catch (SQLException e) {
        //    throw new RuntimeException(e);
        //}
        launch();
        System.out.println("its working");

    }
}