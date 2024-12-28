package lk.ijse.gdse71.mobilezone.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminLogInPageController implements Initializable {
        @FXML
        private Button createAccountOnAction;

        @FXML
        private Button forgetPasswordOnAction;

        @FXML
        private AnchorPane logInPage;

        @FXML
        private TextField txtPassword;

        @FXML
        private TextField txtUserName;

        @FXML
        private ImageView imageViewPane;

        @FXML
        private AnchorPane imageAnchorPane;

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
        void logInOnAction(ActionEvent event) throws IOException {
                Stage stage; // 2
                Parent root; // 2
                String userName = txtUserName.getText();
                String password = txtPassword.getText();

                if(validateCredentials(userName, password)){
                        showAlert("LogIn Successfully! ", "Welcome, " + userName + "!", Alert.AlertType.INFORMATION);


                        String fxmlPath = "/view/MainLayout.fxml";
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

                }else{
                        showAlert("Failed to LogIn! ", "Invalid username or password", Alert.AlertType.ERROR);
                }
        }

        private void showAlert(String title, String message, Alert.AlertType alertType) {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setContentText(message);
                alert.showAndWait();
        }

        private boolean validateCredentials(String userName, String password) {
                String role = "Admin";
                try{
                        ResultSet resultSet = CrudUtil.execute("SELECT * FROM LogInCredentials WHERE role = ? AND userName = ? AND password = ? ", role, userName, password);
                        return resultSet.next();
                } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert("Error!", "An Error occurred!", Alert.AlertType.ERROR);
                        //throw new RuntimeException(e);
                        return false;
                }

        }

        public void createAnAccountOnAction(ActionEvent actionEvent) {
        }

        public void forgetPasswordOnAction(ActionEvent actionEvent) {
        }
}

