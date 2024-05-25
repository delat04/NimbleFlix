package controllers.edit;

import DAO.AccountDAO;
import DAO.MediaDAO;
import DAO.MessageDAO;
import classes.Account;
import classes.MediaObj;
import classes.Message;
import controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.Controller_util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditMedia extends SceneController {
    @FXML
    private TextField title;
    @FXML
    private TextField genre;
    @FXML
    private TextField country;
    @FXML
    private TextField language;
    @FXML
    private TextField seasonnumber;
    @FXML
    private Label id;
    @FXML
    private Label vidpath;
    @FXML
    private ChoiceBox mediatype;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea description;
    @FXML
    private Label errorBox;
    @FXML
    private ImageView profile_pic;


    public void setup(Stage s, Account A, MediaObj M,Account AA){
        stage=s;
        Acc=A;
        Med =M;
        keepopen=true;


        System.out.println(M);
        if(M.getImageUrl() !=""){
            profile_pic.setImage(new Image(getClass().getResource("/imgs")+"/"+ M.getImageUrl()));
        }
        title.setText(M.getName());
        language.setText(M.getLanguage());
        country.setText(M.getCountry());
        genre.setText(M.getGenre());
        id.setText( String.valueOf(M.getId()) );
        vidpath.setText(M.getVideoUrl());
        seasonnumber.setText(String.valueOf(M.getSeasonNumber()));
        mediatype.setValue(M.getType());
        if(M.getStartDate()!=null){
            date.setValue(M.getStartDate().toLocalDate());
        }
        description.setText(M.getDescription());
    }

    @FXML
    private void handleFileUpload(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(((Node)e.getSource()).getScene().getWindow());
        if (selectedFile != null) {

            Path sourcePath = selectedFile.toPath();
            String directory="imgs";
            if(getFileExtension(sourcePath).equals(".mp4")){
                directory="videos";
            }
            System.out.println(getClass().toString());
            String resourcePath = getClass().getResource("/"+directory).getPath();
            Path targetDir = Paths.get(resourcePath);

            try {
                String newname= Acc.getId() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("-ss-hh-dd-mm-yyyy"));
                Files.copy(sourcePath, targetDir.resolve(  newname + getFileExtension(sourcePath) ) );
                if(directory=="imgs"){
                profile_pic.setImage( new Image( getClass().getResource("/imgs")+"/"+directory+"/" + newname +getFileExtension(sourcePath) ) );}
                else {
                    vidpath.setText( newname + getFileExtension(sourcePath) );
                }
            } catch (IOException a) {
                a.printStackTrace();
            }

        }
    }
    private static String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int i = fileName.lastIndexOf('.');
        return (i == -1) ? "" : fileName.substring(i);
    }
    private static String getFilePart(String path) {
        int i = path.lastIndexOf('/');
        return (i == -1) ? "" : path.substring(i+1);
    }


    public void delete_media(ActionEvent e){
        if(delete_button.getText().equals("Are you Sure?")){
            try {
                MediaDAO.deleteMedia(Med.getId());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            Controller_util.switchTocontroller(stage, "ShowAccount", Acc, null,Acc);
        }
        else{
            delete_button.setText("Are you Sure?");
        }

    }
    @FXML
    public void editProfile(ActionEvent e){
        try{
            MediaObj same_name=null;

            same_name= MediaDAO.getMediaByName(title.getText());

            if(same_name !=null && same_name.getId() != Med.getId()){
                errorBox.setText("Another Media with this Title already Exists!");
            }
            else if(date.getValue() == null){
                errorBox.setText("Unvalid Date!");
            }
            else{

                try {
                    Med.setImageUrl(getFilePart(profile_pic.getImage().getUrl()));
                    Med.setName(title.getText());
                    Med.setLanguage(language.getText());
                    Med.setCountry(country.getText());
                    Med.setGenre(genre.getText());
                    //Med.setId(Integer.valueOf(id.getText())); wont change it
                    Med.setVideoUrl(vidpath.getText());
                    Med.setSeasonNumber(Integer.valueOf(seasonnumber.getText()));
                    Med.setType((String) mediatype.getValue());
                    Med.setStartDate(Date.valueOf(date.getValue()));
                    Med.setDescription(description.getText());
                    if(Med.getId()==0){
                        MediaDAO.addMedia(Med);
                        Med = MediaDAO.getMediaByName(Med.getName());
                        MessageDAO.addMessage(new Message(0,"","produces serie",Acc.getId(),Med.getId()));
                    }
                    else{
                        MediaDAO.updateMedia(Med);
                        Med = MediaDAO.getMediaByName(Med.getName());
                    }
                    Controller_util.switchTocontroller(stage, "ShowMedia", Acc, Med,null);
                } catch (Exception ex) {
                    errorBox.setText("Make sure to fill the required inputs !");
                    System.out.println( ex.fillInStackTrace() );
                }

            }

        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
