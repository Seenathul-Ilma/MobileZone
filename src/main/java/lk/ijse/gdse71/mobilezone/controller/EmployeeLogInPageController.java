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

public class EmployeeLogInPageController implements Initializable {

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
    private MainLayoutController mainLayoutController;

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

                /*ImageView imageView = new ImageView();

                // Load an image (make sure to replace "image_path.png" with your image file path)
                Image image = new Image(getClass().getResourceAsStream("/Images/background.jpg"));
                imageView.setImage(image);

                // Preserve the aspect ratio of the image
                imageView.setPreserveRatio(true);

                // Bind the ImageView's fitWidth and fitHeight to the AnchorPane's size
                AnchorPane root = new AnchorPane(imageView);
                imageView.fitWidthProperty().bind(root.widthProperty());
                imageView.fitHeightProperty().bind(root.heightProperty());
                imageView.setPickOnBounds(true);

                // Create the Scene and set it on the Stage
                Scene scene = new Scene(root, 600, 500);

                stage.setTitle("Responsive Image Example");
                stage.setScene(scene);
                stage.show();*/


        if(validateCredentials(userName, password)){
            showAlert("LogIn Successfully! ", "Welcome, " + userName + "!", Alert.AlertType.INFORMATION);
                        /*logInPage.getChildren().clear();
                        logInPage.setVisible(false);

                        String fxmlPath = "/view/MainLayout.fxml";  // 1
                        AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));  // 1
                        //root = FXMLLoader.load(getClass().getResource("/view/MainLayout.fxml"));  // 2
                        Scene scene = new Scene(load); // 1,2
                        stage = new Stage(); // 1,2
                        stage.setScene(scene); // 1,2
                        stage.show(); // 1,2*/

            String fxmlPath = "/view/EmployeeMainLayout.fxml";
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
        String role = "Employee";
        try{
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM LogInCredentials WHERE role = ? AND userName = ? AND password = ?", role, userName, password);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error!", "An Error occurred!", Alert.AlertType.ERROR);
            //throw new RuntimeException(e);
            return false;
        }
    }
}

