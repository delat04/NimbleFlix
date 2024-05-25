package controllers.show;

import DAO.AccountDAO;
import DAO.MediaDAO;
import DAO.MessageDAO;
import classes.Account;
import classes.MediaObj;
import classes.Message;
import controllers.SceneController;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.action.Action;
import javafx.scene.text.Text;
import service.Controller_util;

import java.io.File;
import java.sql.SQLException;

public class ShowAccount extends SceneController {
    @FXML
    private Label fullname;
    @FXML
    private Label username;
    @FXML
    private Label email;
    @FXML
    private Label date;
    @FXML
    private Text aboutme;
    @FXML
    private Label type_mark;
    @FXML
    private Label id;
    @FXML
    private Button type_circle;
    @FXML
    private ImageView profile_pic;
    @FXML
    private FlowPane actors;
    @FXML
    private FlowPane series;

    @FXML
    private GridPane obb;

    public void setup(Stage s, Account A, MediaObj M,Account AA){
        Acc=A;
        Aac=AA;
        stage=s;
        keepopen=true;
        profile_pic.setImage(new Image("file:C:/Users/user/Downloads/proj/NimbleFlix/src/main/resources/imgs/"+AA.getProfilePic()));
        fullname.setText(AA.getFullName());
        username.setText(AA.getUsername());
        id.setText(String.valueOf(AA.getId()));
        username.setStyle("-fx-text-fill: "+type_colors.get(AA.getType())+";");
        type_mark.setText(AA.getType());
        type_circle.setStyle("-fx-background-color:"+type_colors.get(AA.getType())+";");
        type_mark.setStyle("-fx-text-fill: "+type_colors.get(AA.getType())+";");
        email.setText(AA.getEmail());
        if(AA.getDateOfBirth()!=null)
            date.setText(AA.getDateOfBirth().toString());
        aboutme.setText(AA.getAboutMe());
        try {
            System.out.println(AccountDAO.getActorsSubsribedTo(AA.getId()));
            listAccounts(AccountDAO.getActorsSubsribedTo(AA.getId()) , actors );
            listMedias(AccountDAO.getSeriesSubsribedTo(AA.getId()) , series );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            switch (A.getType()) {
                case "User" -> {
                    if (AA.getType().equals("Actor")) {
                        if (!AccountDAO.isSubscribedActor(A.getId(), AA.getId())) {
                            Button subsribe = new Button();
                            subsribe.setText("Subscribe");
                            subsribe.getStyleClass().add("subscribe");
                            subsribe.setOnAction(event -> {
                                        subsribe_actor(A.getId(), AA.getId());
                                    }

                            );
                            obb.add(subsribe, 0, 0);
                        } else {
                            Button subsribe = new Button();
                            subsribe.setText("UnSubscribe");
                            subsribe.getStyleClass().add("unsubscribe");
                            subsribe.setOnAction(event -> {
                                unsubsribe_actor(A.getId(), AA.getId());
                            });
                            obb.add(subsribe, 0, 0);
                        }
                    }
                }
                case "Administrator" -> {
                    Button subsribe = new Button();
                    subsribe.setText("Edit");
                    subsribe.getStyleClass().add("edit_button");
                    subsribe.setOnAction(event -> {
                        Controller_util.switchTocontroller(stage, "EditAccount", Acc, null, AA);
                    });
                    obb.add(subsribe, 0, 0);

                }
                case "Producer" -> {
                    if(A.getId() == AA.getId()) {
                        Button subsribe = new Button();
                        subsribe.setText("Add Serie");
                        subsribe.getStyleClass().add("subscribe");
                        subsribe.setOnAction(event -> {
                            Controller_util.switchTocontroller(stage, "EditMedia", Acc,
                                    new MediaObj(0, "Serie", 0, null, null, null, 0, null, null, null, null, null)
                                    , null);
                        });
                        obb.add(subsribe, 0, 0);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
/*
        try{
            listMedias(MediaDAO.getAllMedia() , actors );
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

    }


    public void subsribe_actor(int idActor,int idMedia){
        try {
            MessageDAO.addMessage(new Message(0,"","subscribed to actor",idActor,idMedia));
            Controller_util.switchTocontroller(stage, "ShowAccount", Acc, Med,Aac);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void unsubsribe_actor(int idActor,int idMedia){
        try {
            MessageDAO.deleteLink(idActor, idMedia,"subscribed to actor");

            Controller_util.switchTocontroller(stage, "ShowAccount", Acc, Med,Aac);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
