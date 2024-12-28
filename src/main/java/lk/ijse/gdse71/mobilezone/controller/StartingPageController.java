package lk.ijse.gdse71.mobilezone.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartingPageController implements Initializable {
    @FXML
    private AnchorPane imageAnchorPane;

    @FXML
    private ImageView imageViewPane;

    @FXML
    private AnchorPane logInPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load your image into the ImageView
        Image image = new Image(getClass().getResourceAsStream("/Images/mobileZoneBackground.jpg")); // Replace with the actual path to your image
        imageViewPane.setImage(image);


        // Preserve the image's aspect ratio
        imageViewPane.setPreserveRatio(true);

        // Bind the ImageView's dimensions to the parent pane's dimensions
        imageViewPane.fitWidthProperty().bind(imageAnchorPane.widthProperty());
        imageViewPane.fitHeightProperty().bind(imageAnchorPane.heightProperty());
    }

    @FXML
    void logInAsAdminOnAction(ActionEvent event) {
        navigateTo("/view/AdminLogInPage.fxml");
    }

    @FXML
    void logInAsEmployeeOnAction(ActionEvent event) {
        navigateTo("/view/EmployeeLogInPage.fxml");
    }


    private void navigateTo(String fxmlPath) {
        try {
            logInPage.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

            load.prefWidthProperty().bind(logInPage.widthProperty());
            load.prefHeightProperty().bind(logInPage.heightProperty());

            logInPage.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }
}
