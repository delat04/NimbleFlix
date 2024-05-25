package controllers.authentication;
import DAO.AccountDAO;
import DAO.MediaDAO;
import DAO.MessageDAO;
import DB.DB;
import classes.Account;
import classes.MediaObj;
import controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.HelloApplication;
import service.Controller_util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Login extends SceneController {

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    private Label errorBox;

    @FXML
    private ImageView nimblee;
    @FXML
    private Pane cover;

    public void setup(Stage s, Account A, MediaObj M, Account AA){
        stage=s;/*
        stage=new Stage();
        Acc=A;
        Aac=AA;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 0, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add("file:src/main/resources/main/styles.css");
        stage.setScene(scene);
        stage.show();
*/

        System.out.println(Aac);
        nimblee.setImage(new Image("file:src/main/resources/imgs/nimblee.png"));
        cover.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                nimblee.setStyle("-fx-opacity:1;");
            }
        });

        cover.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                nimblee.setStyle("-fx-opacity:0;");
            }
        });
        //flix.setImage(new Image("file:src/main/resources/imgs/flix.png"));
    }

    @FXML
    public void login(ActionEvent e){
        try{
            Account account= AccountDAO.getAccountByUsername(username.getText());
            if(account ==null){
                errorBox.setText("No Account with this username exists!");
            }
            else{
                if(!password.getText().equals(account.getPassword())){
                    errorBox.setText("Wrong password for "+account.getFullName()+"!");
                }
                else {
                    stage.close();
                    stage=new Stage();
                    Controller_util.switchTocontroller(stage, "main", account, null, null);
                }

            }

        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
    public void resett(ActionEvent e){
        username.setText("");
        password.setText("");
    }
}
