package controllers.show;

import DAO.AccountDAO;
import DAO.MediaDAO;
import DAO.MessageDAO;
import classes.Account;
import classes.MediaObj;
import classes.Message;
import controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import service.Controller_util;
import javafx.scene.shape.Polygon;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ShowMedia extends SceneController {

    @FXML
    private MediaView mediaview;
    @FXML
    private Label type_label;
    @FXML
    private Label name_label;
    @FXML
    private Button back_label;

    @FXML
    private FlowPane description;
    @FXML
    private VBox comments;
    @FXML
    private VBox inner_comments;
    @FXML
    private FlowPane episodes;
    @FXML
    private Button playButton;
    @FXML
    private Slider slider;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextArea mycomment;
    @FXML
    private GridPane obb;
    public void setup(Stage s,Account A,MediaObj M,Account AA){
        Acc=A;
        Med=M;
        stage=s;
        keepopen=false;

        description.toFront();

        //System.out.println(new File("src/main/resources/videos/vid.mp4").toURI().toString());
        Media media = new Media(new File(getClass().getResource("/videos")+"/"+M.getVideoUrl()).toURI().toString());
        //Media media = new Media(new File("src/main/resources/videos/vid.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaview.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();

        playButton.setOnAction(event -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        });
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue());
        });
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!progressBar.isPressed()) {
                double progress = newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                progressBar.setProgress(progress);
            }
        });

        progressBar.setOnMouseClicked((MouseEvent event) -> {
            double position = event.getX() / progressBar.getWidth();
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(position));
        });

        try {
            switch (A.getType()) {
                case "User":
                    if (!AccountDAO.isSubscribed(A.getId(), M.getId())) {
                        Button subsribe = new Button();
                        subsribe.setText("Subscribe");
                        subsribe.getStyleClass().add("subscribe");
                        subsribe.setOnAction(event -> {
                                    subsribe_media(A.getId(), M.getId());
                                }

                        );
                        obb.add(subsribe, 0, 0);
                    }else{
                        Button subsribe = new Button();
                        subsribe.setText("UnSubscribe");
                        subsribe.getStyleClass().add("unsubscribe");
                        subsribe.setOnAction(event -> {
                            unsubsribe_media(A.getId(),M.getId());
                        });
                        obb.add(subsribe, 0, 0);
                    }
                    break;
                case "Producer":
                    if (AccountDAO.didProduceMedia(A.getId(), M.getId())) {
                        Button subsribe = new Button();
                        subsribe.setText("Edit");
                        subsribe.getStyleClass().add("edit_button");
                        subsribe.setOnAction(event -> {
                            if(mediaPlayer!=null){
                                mediaPlayer.stop();
                                mediaPlayer.dispose();
                            }
                            Controller_util.switchTocontroller(stage, "EditMedia", Acc, M,null);
                                });
                        obb.add(subsribe, 0, 0);
                    }
                    break;
                case "Administrator":
                    Button subsribe = new Button();
                    subsribe.setText("Edit");
                    subsribe.getStyleClass().add("edit_button");
                    subsribe.setOnAction(event -> {
                        if(mediaPlayer!=null){
                            mediaPlayer.stop();
                            mediaPlayer.dispose();
                        }
                        Controller_util.switchTocontroller(stage, "EditMedia", Acc, M,null);
                    });
                    obb.add(subsribe, 0, 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        type_label.setText(M.getType()+":");
        name_label.setText(M.getName());
        MediaObj parent;
        try{
            parent = MediaDAO.getMediaById(M.getParentId());
            if(parent != null) {
                back_label.setText("Back to " + parent.getName());
                back_label.setOnAction((e) -> {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                            Controller_util.switchTocontroller(stage, "ShowMedia", Acc, parent,null);
                        }
                );
            }

        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ///listing stuff
        try{
            if( Med.getType().equals("Episode")){
                listMedias(MediaDAO.getMediasByMedia(parent) , episodes );
            }
            else{
                listMedias(MediaDAO.getMediasByMedia(Med) , episodes );
            }
            listComments(  MediaDAO.getCommentsByMedia(Med)  , inner_comments );
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        super.initialize();
        mediaview.setPreserveRatio(false);
        mediaview.fitWidthProperty().bind(scrollpane.widthProperty());
        mediaview.fitHeightProperty().bind(scrollpane.widthProperty().divide(2));

        Polygon triangle = new Polygon(0, 0, 20, 10, 0, 20);
        playButton.setShape(triangle);
        playButton.setPrefSize(20, 20);

    }

    public void toDescription(){
        description.toFront();
    }
    public void toComments(){
        comments.toFront();
    }
    public void toEpisodes(){
        episodes.toFront();
    }


    public void postcomment(ActionEvent e) {
        mycomment.getText();
        try {
            if(!mycomment.getText().equals("")) {
                MessageDAO.addMessage(new Message(0, mycomment.getText(), "comment", Acc.getId(), Med.getId()));
                mycomment.setText("");

                listComments(  MediaDAO.getCommentsByMedia(Med)  , inner_comments );
            }

            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void subsribe_media(int idActor,int idMedia){
        try {
            MessageDAO.addMessage(new Message(0,"","subscribed to serie",idActor,idMedia));
            mediaPlayer.stop();
            mediaPlayer.dispose();
            Controller_util.switchTocontroller(stage, "ShowMedia", Acc, Med,null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void unsubsribe_media(int idActor,int idMedia){
        try {
            MessageDAO.deleteLink(idActor, idMedia,"subscribed to serie");
            mediaPlayer.stop();
            mediaPlayer.dispose();
            Controller_util.switchTocontroller(stage, "ShowMedia", Acc, Med,null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
