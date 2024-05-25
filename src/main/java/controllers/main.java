package controllers;

import DAO.AccountDAO;
import DAO.MediaDAO;
import classes.Account;
import classes.MediaObj;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Date;
import java.sql.SQLException;

public class main extends SceneController {
    @FXML
    private TextField keyword;
    @FXML
    private Label errorBox;
    @FXML
    private TextField year;
    @FXML
    private DatePicker date1;
    @FXML
    private DatePicker date2;
    @FXML
    private TextField genre;

    @FXML
    private FlowPane actors;
    @FXML
    private FlowPane series;

    @FXML
    private ImageView background;
    public void setup(Stage s, Account A, MediaObj M, Account AA) {
        Acc = A;
        Aac = AA;
        stage = s;
        keepopen = true;
        try {
            //listAccounts(AccountDAO.getAllAccounts().stream().filter(e->!e.getType().equals("Actor")).toList() , actors );
            listMedias(MediaDAO.getAllMedia().stream().filter(e -> e.getType().equals("Serie")).toList(), series);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        background.setImage(new Image("file:src/main/resources/imgs/bb.jpg"));
        background.fitWidthProperty().bind(scrollpane.widthProperty());
    }
    public void listMedias(ActionEvent e){
        //int 0 year, Date null, Date null , String null title,String null genre

        int y=0;
        errorBox.setText("");
        Date startdate=null;
        Date enddate=null;
        try{
            if(year.getText().equals("")){
                y=0;
            }
            else{
                y=Integer.valueOf(year.getText() );
            }
        } catch (NumberFormatException ex) {
            errorBox.setText("Wrong input for year ");
        }
        if(date1.getValue()!=null)
            startdate = Date.valueOf(date1.getValue());

        if(date2.getValue()!=null)
            enddate = Date.valueOf(date2.getValue());

        try {
            listMedias(searchMedias(
                    y,
                    startdate,
                    enddate,
                    keyword.getText(),
                    genre.getText()

            ),series);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
