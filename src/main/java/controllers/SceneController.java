package controllers;

import DAO.AccountDAO;
import DAO.MediaDAO;
import DB.DB;
import classes.Account;
import classes.MediaObj;
import classes.Message;
import controllers.show.Comment;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.Controller_util;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import main.HelloApplication;
public class SceneController{
    protected Account Acc;
    protected Account Aac;
    protected MediaObj Med;
    protected Stage stage;

    protected boolean keepopen=false;
    protected MediaPlayer mediaPlayer;
    @FXML
    protected Button toggle_menu_button;
    @FXML
    private HBox sidebar;
    @FXML
    protected ScrollPane scrollpane;

    @FXML
    protected Button delete_button;


    protected HashMap<String,String> type_colors= new HashMap<String,String>(){{
        put("Producer","#8f0c0c");
        put("Actor","#ffc107");
        put("User","#1160ab");
    }};


     public void setup(Stage stage,Account A, MediaObj M,Account AA){
         System.out.println("b");
    }
    @FXML
    public void initialize() {
        Timer timer = new Timer();
        if(sidebar!=null){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!keepopen) {
                    Platform.runLater(() -> toggle_menu(new ActionEvent()));
                }
            }
        }, 500);
        }
    }

    public void toggle_menu(ActionEvent e){
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), sidebar);
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.3), scrollpane);
        if (sidebar.getTranslateX() != 0) {
            toggle_menu_button.setText(">>>");
            transition.setFromX(-sidebar.widthProperty().getValue());
            transition.setToX(0);
            transition.play();
            transition2.setFromX(-sidebar.widthProperty().getValue());
            transition2.setToX(0);
            transition2.play();

        } else {
            toggle_menu_button.setText("<<<");
            transition.setFromX(0);
            transition.setToX(-sidebar.widthProperty().getValue());
            transition.play();
            transition2.setFromX(0);
            transition2.setToX(-sidebar.widthProperty().getValue());
            transition2.play();

        }
    }
    public void exit_(ActionEvent e){
        Stage stage = (Stage) toggle_menu_button.getScene().getWindow();
        stage.close();
    }
    public void switch_to_profile(ActionEvent e){
         if(mediaPlayer!=null){
        mediaPlayer.stop();
        mediaPlayer.dispose();
         }
            System.out.println(":+"+Acc);
            Controller_util.switchTocontroller(stage,"ShowAccount",Acc, Med,Acc);
    }
    /*public void switch_to_home(ActionEvent e){

        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        try{
            Controller_util.switchTocontroller(stage,"ShowAccount",Acc, Med);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }*/
    public void switch_to_edit_profile(ActionEvent e){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

            Controller_util.switchTocontroller(stage,"EditAccount",Acc, Med,Acc);
    }

    public void getToRegister(ActionEvent  e){
        Controller_util.switchTocontroller(stage,"register",Acc, Med,Acc);
    }
    public void toMain(ActionEvent e){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Controller_util.switchTocontroller(stage,"main",Acc, null,null);
    }

    public void listMedias(List<MediaObj> AS , FlowPane fp){
            fp.getChildren().clear();
            if(AS != null){
                AS.stream()
                        .map(
                                this::presentMedia
                        )
                        .forEach(
                                m->fp.getChildren().add(m)
                        );
            }
    }

    public void listComments(List<Message> AS , VBox fp ) throws IOException {
        fp.getChildren().clear();
        if(AS != null){
            AS.stream()
                    .map(
                            message -> {
                                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("comment.fxml"));
                                Pane commentPane =null;
                                try {
                                    commentPane = loader.load();
                                Comment commentController = loader.getController();
                                Account source = AccountDAO.getAccountById(message.getSending_object_id());
                                commentController.setProfilePic( source.getProfilePic() );

                                commentController.setFullName( source.getFullName() );

                                commentController.setAboutMe( message.getContent());
                                commentController.setCircle(type_colors.get(source.getType()));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                return commentPane;
                            }
                    )
                    .forEach(
                            m->fp.getChildren().add(m)
                    );
        }
    }
    public void listAccounts(List<Account> AS , FlowPane fp ){
        if(AS != null){
            AS.stream()
                    .map(
                            this::presentAccount
                    )
                    .forEach(
                            m->fp.getChildren().add(m)
                    );
        }
    }/*
    public void listMessages(ArrayList<Message> AS , FlowPane fp){
        if(AS != null){
            AS.stream()
                    .map(
                            this::presentMessage
                    )
                    .forEach(
                            m->fp.getChildren().add(m)
                    );
        }
    }*/


    public VBox presentMedia(MediaObj M){
        VBox vb = new VBox();
        ImageView imageView = new ImageView("file:src/main/resources/imgs/"+M.getImageUrl());
        vb.getStyleClass().add("small_img");
        imageView.setFitWidth(150);
        imageView.setFitHeight(200);

        Label name = new Label(M.getName());
        name.getStyleClass().add("name_label");

        vb.getChildren().addAll(imageView, name);


        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER_LEFT);
        vb.getStyleClass().add("vb");
        vb.setOnMouseClicked(event -> {
            if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            Controller_util.switchTocontroller(stage, "ShowMedia", Acc , M,null);
        });
        return vb;
    }

    public VBox presentAccount(Account A){
        VBox vb = new VBox();
        ImageView imageView = new ImageView("file:src/main/resources/imgs/"+A.getProfilePic());
        vb.getStyleClass().add("small_img");
        imageView.setFitWidth(150);
        imageView.setFitHeight(200);

        Label name = new Label(A.getFullName());
        name.getStyleClass().add("name_label");

        vb.getChildren().addAll(imageView, name);


        vb.setSpacing(50);
        vb.setAlignment(Pos.CENTER_LEFT);
        vb.getStyleClass().add("vb");
        vb.setOnMouseClicked(event -> {
            if(mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            Controller_util.switchTocontroller(stage, "ShowAccount", Acc , Med, A);
        });
        return vb;
    }


    /*public VBox presentMessage(Account M){
        VBox vb = new VBox();
        ImageView imageView = new ImageView("file:src/main/resources/imgs/"+M.);
        vb.getStyleClass().add("small_img");
        imageView.setFitWidth(150);
        imageView.setFitHeight(200);

        Label name = new Label(M.getFullName());
        name.getStyleClass().add("name_label");

        vb.getChildren().addAll(imageView, name);


        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER_LEFT);
        vb.getStyleClass().add("vb");
        vb.setOnMouseClicked(event -> {
            Controller_util.switchTocontroller(stage, "ShowMedia", Acc , M);
        });
        return vb;
    }*/



    /*SELECT *
FROM your_table
WHERE EXTRACT(YEAR FROM your_date) = 2023
AND your_date BETWEEN TO_DATE('2023-01-01', 'YYYY-MM-DD') AND TO_DATE('2023-12-31', 'YYYY-MM-DD')
AND title LIKE 'keyword%'
AND genre LIKE 'genre%';
     */
    public List<MediaObj> searchMedias(int year, Date startDate, Date endDate , String title, String genre) throws SQLException {
        List<MediaObj> medias=new ArrayList<>();
        String query = "SELECT * FROM media " +
                "WHERE type = 'Serie' ";
        if( year != 0){
            query+=  " AND EXTRACT(YEAR FROM start_date) = ? ";
        }
        if(startDate!=null && endDate!=null){
            query+=" AND start_date BETWEEN TO_DATE( ? , 'YYYY-MM-DD') AND TO_DATE( ? , 'YYYY-MM-DD')\n";
        }
        if(title!="") {
            query += " AND name LIKE ? ";
        }
        if(genre!="") {
            query += " AND genre LIKE ? ";
        }
        query+="";
        PreparedStatement statement = DB.getInstance().prepareStatement(query);


        System.out.println(query);
        int i=1;

        if( year != 0){
            statement.setInt(i,year);
            i++;
        }
        if(startDate!=null && endDate!=null){
            statement.setString(i,startDate.toString());
            i++;
            statement.setString(i,endDate.toString());
            i++;
        }
        if(!title.equals("")) {
            System.out.println(">"+title);
            statement.setString(i,title+"%");
            i++;
        }
        if(!genre.equals("")) {
            statement.setString(i,genre+"%");
            i++;
        }

        ResultSet rs = statement.executeQuery();



        System.out.println("ffff"+startDate);
        System.out.println("ffff2"+endDate);
        while (rs.next()) {
            MediaObj media = MediaObj.setSelfFromResult(rs);
            medias.add(media);
            System.out.println(medias);
        }
        System.out.println(medias);
        return medias;

    }





}
