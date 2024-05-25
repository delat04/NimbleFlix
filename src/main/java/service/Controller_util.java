package service;
import classes.Account;
import classes.MediaObj;
import main.HelloApplication;
import controllers.SceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller_util extends  SceneController{

        // switchToController('name', Account currentUser , Object additional_object (media/account/message)

    public static void switchTocontroller(Stage stage,String controller, Account user, MediaObj mediaObj, Account account) {

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource( controller+".fxml" ));
        Parent root=null;
        try{
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SceneController a = loader.getController();
        System.out.println(user);
        a.setup(stage,user, mediaObj,account);

        //root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
        //Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("file:src/main/resources/main/styles.css");
        stage.setScene(scene);
        stage.show();
    }
}
