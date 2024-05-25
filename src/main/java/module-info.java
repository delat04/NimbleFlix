module com.nimbleflix.nimbleflix {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    exports controllers.show;
    exports controllers.authentication;

    //opens com.nimbleflix.nimbleflix to javafx.fxml;
    //exports com.nimbleflix.nimbleflix;
    exports main;
    opens controllers.show to javafx.fxml;
    opens controllers.edit to javafx.fxml;
    opens controllers.authentication to javafx.fxml;
    exports controllers.edit;
    opens controllers to javafx.fxml;
    exports controllers;
}