package lk.ijse.gdse71.mobilezone.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    @FXML private AnchorPane content;
    @FXML private AnchorPane firstAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        navigateTo("/view/Dashboard.fxml");
    }

    @FXML
    void navigateToCustomerPage(ActionEvent event) {
        navigateTo("/view/CustomerView.fxml");
    }

    @FXML
    void navigateToEmployeePage(ActionEvent event) {
        navigateTo("/view/EmployeeView.fxml");
    }

    @FXML
    void navigateToExpensePage(ActionEvent event) {
        navigateTo("/view/ExpenseView.fxml");
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

    @FXML
    void navigateToLogInCredentialPage(ActionEvent event) {
        navigateTo("/view/LogInCredentials.fxml");
    }

    @FXML
    void navigateToPurchasePage(ActionEvent event) {
        navigateTo("/view/PurchaseView.fxml");
    }

    @FXML
    void navigateToSupplierPage(ActionEvent event) {
        navigateTo("/view/SupplierView.fxml");
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
    public void navigateToPurchaseReturnPage(ActionEvent event) {
        navigateTo("/view/PurchaseReturnView.fxml");
    }

    @FXML
    public void navigateToLogout(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {
            try {
                firstAnchorPane.getChildren().clear();
                AnchorPane load = FXMLLoader.load(getClass().getResource("/view/StartingPage.fxml"));

                load.prefWidthProperty().bind(firstAnchorPane.widthProperty());
                load.prefHeightProperty().bind(firstAnchorPane.heightProperty());

                firstAnchorPane.getChildren().add(load);
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to load page!").show();
            }
        }
    }
}
