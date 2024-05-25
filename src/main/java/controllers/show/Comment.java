package controllers.show;
import controllers.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Comment extends SceneController {

    @FXML
    private ImageView profile_pic;

    @FXML
    private Label fullname;

    @FXML
    private Text aboutme;

    @FXML
    private Button type_circle;
    public void setProfilePic(String path) {
        profile_pic.setImage(new Image("file:src/main/resources/imgs/"+path));
    }

    public void setCircle(String color){
        type_circle.setStyle("-fx-background-color:"+color+";");
    }
    public void setFullName(String name) {
        fullname.setText(name);
    }

    public void setAboutMe(String text) {
        aboutme.setText(text);
    }
}
