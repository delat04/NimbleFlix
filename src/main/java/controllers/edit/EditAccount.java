package controllers.edit;

import DAO.AccountDAO;
import classes.Account;
import classes.MediaObj;
import controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.Controller_util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class EditAccount extends SceneController {
    @FXML
    private TextField fullname;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label id;
    @FXML
    private TextField email;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea aboutme;
    @FXML
    private Label type_mark;
    @FXML
    private Label errorBox;
    @FXML
    private Button type_circle;
    @FXML
    private ImageView profile_pic;
    public void setup(Stage s, Account A, MediaObj M,Account AA){
        stage=s;
        Acc=A;
        Aac=AA;
        keepopen=true;
        System.out.println(Aac);
        profile_pic.setImage(new Image("file:C:/Users/user/Downloads/proj/NimbleFlix/src/main/resources/imgs/"+Aac.getProfilePic()));
        fullname.setText(Aac.getFullName());
        username.setText(Aac.getUsername());
        password.setText(Aac.getPassword());
        id.setText( String.valueOf(Aac.getId()) );
        type_mark.setText(Aac.getType());
        type_circle.setStyle("-fx-background-color:"+type_colors.get(Aac.getType())+";");
        type_mark.setStyle("-fx-text-fill: "+type_colors.get(Aac.getType())+";");
        email.setText(Aac.getEmail());
        if(Aac.getDateOfBirth()!=null)
            date.setValue(Aac.getDateOfBirth().toLocalDate());
        aboutme.setText(Aac.getAboutMe());
    }

    @FXML
    private void handleFileUpload(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(((Node)e.getSource()).getScene().getWindow());
        if (selectedFile != null) {

            Path sourcePath = selectedFile.toPath();

            Path targetDir = Paths.get("C:\\Users\\user\\Downloads\\proj\\NimbleFlix\\src\\main\\resources\\imgs");

            try {
                String newname = Aac.getId() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("-ss-hh-dd-mm-yyyy"));
                System.out.println(sourcePath);
                System.out.println(targetDir.resolve(newname + getFileExtension(sourcePath)));

                Files.copy(sourcePath, targetDir.resolve(newname + getFileExtension(sourcePath)));
                profile_pic.setImage(new Image((targetDir.resolve(newname + getFileExtension(sourcePath))).toString()));
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


    @FXML
    public void editProfile(ActionEvent e){
        try{
            Account same_name= AccountDAO.getAccountByUsername(username.getText());
            if(same_name !=null && same_name.getId() != Aac.getId()){
                errorBox.setText("Another Account with this Username already Exists!");
            }
            else if(date.getValue() == null){
                errorBox.setText("Unvalid Date!");
            }
            else{

                //username = ?,password= ?, type = ?, full_name = ?, profile_pic = ?, email = ?, date_of_birth = ?, about_me = ?
                Aac.setFullName(fullname.getText());
                Aac.setUsername(username.getText());
                Aac.setPassword(password.getText());
                Aac.setEmail(email.getText());
                Aac.setDateOfBirth(Date.valueOf(date.getValue()));
                Aac.setAboutMe(aboutme.getText());

                Aac.setProfilePic(getFilePart(profile_pic.getImage().getUrl()));
                AccountDAO.updateAccount(Aac);

                System.out.println(AccountDAO.getAllAccounts());

                    Controller_util.switchTocontroller(stage, "ShowAccount", Acc, null,Aac);

            }

        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
