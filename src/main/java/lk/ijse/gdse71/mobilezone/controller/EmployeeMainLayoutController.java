package lk.ijse.gdse71.mobilezone.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMainLayoutController implements Initializable {
    @FXML
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigateTo("/view/OrdersView.fxml");
    }

    @FXML
    void navigateToCustomerPage(ActionEvent event) {
        navigateTo("/view/CustomerView.fxml");
    }

    @FXML
    void navigateToItemCategoryPage(ActionEvent event) {
        navigateTo("/view/CategoryView.fxml");
    }

    @FXML
    void navigateToOrderReturnPage(ActionEvent event){
        navigateTo("/view/OrderReturnView.fxml");
    }

    @FXML
    void navigateToItemPage(ActionEvent event) {
        navigateTo("/view/ItemView.fxml");
    }

    @FXML
    void navigateToOrderPage(ActionEvent event) {
        navigateTo("/view/OrdersView.fxml");
    }
    private void navigateTo(String fxmlPath) {
        try {
            content.getChildren().clear();
            AnchorPane load = FXMLLoader.load(getClass().getResource(fxmlPath));

            load.prefWidthProperty().bind(content.widthProperty());
            load.prefHeightProperty().bind(content.heightProperty());

            content.getChildren().add(load);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
        }
    }

    @FXML
    public void navigateToDashboard(ActionEvent event) {
        navigateTo("/view/DashBoard.fxml");
    }

    @FXML
    public void navigateToLogout(ActionEvent event){
        navigateTo("/view/StartingPage.fxml");
    }
}
