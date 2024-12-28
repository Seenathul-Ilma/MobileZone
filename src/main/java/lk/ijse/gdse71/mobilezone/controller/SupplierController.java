package lk.ijse.gdse71.mobilezone.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.gdse71.mobilezone.dto.SupplierDTO;
import lk.ijse.gdse71.mobilezone.dto.tm.SupplierTM;
import lk.ijse.gdse71.mobilezone.model.SupplierModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<SupplierTM, String> colAddress;

    @FXML
    private TableColumn<SupplierTM, String> colNic;

    @FXML
    private TableColumn<SupplierTM, String> colCompany;

    @FXML
    private TableColumn<SupplierTM, String> colContactPerson;

    @FXML
    private TableColumn<SupplierTM, String> colEmail;

    @FXML
    private TableColumn<SupplierTM, String> colPhone;

    @FXML
    private TableColumn<SupplierTM, String> colSupplierId;

    @FXML
    private Label lblSupplierId;

    @FXML
    private TableView<SupplierTM> tblSupplier;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCompany;

    @FXML
    private TextField txtContactPerson;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        colContactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load supplier Id!").show();
        }
    }

    private void refreshPage() throws SQLException {
        loadNextSupplierId();
        loadTableData();

        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        txtCompany.setText("");
        txtContactPerson.setText("");
        txtNic.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
    }

    SupplierModel supplierModel = new SupplierModel();
    private void loadTableData() throws SQLException {
        ArrayList<SupplierDTO> supplierDTOS = supplierModel.getAllSuppliers();
        ObservableList<SupplierTM> supplierTMS = FXCollections.observableArrayList();

        for(SupplierDTO supplierDTO : supplierDTOS){
            SupplierTM supplierTM = new SupplierTM(
                    supplierDTO.getSupplierId(),
                    supplierDTO.getCompanyName(),
                    supplierDTO.getContactPerson(),
                    supplierDTO.getNic(),
                    supplierDTO.getAddress(),
                    supplierDTO.getEmail(),
                    supplierDTO.getPhone()
            );
            supplierTMS.add(supplierTM);
        }
        tblSupplier.setItems(supplierTMS);
    }

    private void loadNextSupplierId() throws SQLException {
        String nextSupplierId = supplierModel.getNextSupplierId();
        lblSupplierId.setText(nextSupplierId);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String supplierId = lblSupplierId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this supplier?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if(optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){
            boolean isDeleted = supplierModel.deleteSupplier(supplierId);
            if(isDeleted){
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully!").show();
            } else{
                new Alert(Alert.AlertType.ERROR, "Failed to delete supplier!").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String supplierId = lblSupplierId.getText();
        String companyName = txtCompany.getText();
        String contactPerson = txtContactPerson.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        txtCompany.setStyle(txtCompany.getStyle() + ";-fx-border-color: #7367F0;");
        txtContactPerson.setStyle(txtContactPerson.getStyle() + ";-fx-border-color: #7367F0;");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");

        String companyPattern = "^[A-Za-z0-9][A-Za-z0-9 &.'-]{1,48}[A-Za-z0-9]$";
        String contactPersonNamePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String addressPattern = "^[A-Za-z0-9][A-Za-z0-9 ,.#-]{3,98}[A-Za-z0-9]$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidCompany = companyName.matches(companyPattern);
        boolean isValidName = contactPerson.matches(contactPersonNamePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);

        if(!isValidCompany){
            txtCompany.setStyle(txtCompany.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidName) {
            txtContactPerson.setStyle(txtContactPerson.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidNic) {
            txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if(!isValidAddress){
            txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }


        if (isValidCompany && isValidName && isValidNic && isValidAddress && isValidEmail && isValidPhone) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    supplierId,
                    companyName,
                    contactPerson,
                    nic,
                    address,
                    email,
                    phone
            );

            boolean isSaved = supplierModel.saveSupplier(supplierDTO);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplier saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save supplier...!").show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String supplierId = lblSupplierId.getText();
        String companyName = txtCompany.getText();
        String contactPerson = txtContactPerson.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        txtCompany.setStyle(txtCompany.getStyle() + ";-fx-border-color: #7367F0;");
        txtContactPerson.setStyle(txtContactPerson.getStyle() + ";-fx-border-color: #7367F0;");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");

        String companyPattern = "^[A-Za-z0-9][A-Za-z0-9 &.'-]{1,48}[A-Za-z0-9]$";
        String contactPersonNamePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String addressPattern = "^[A-Za-z0-9][A-Za-z0-9 ,.#-]{3,98}[A-Za-z0-9]$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        boolean isValidCompany = companyName.matches(companyPattern);
        boolean isValidName = contactPerson.matches(contactPersonNamePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);

        if(!isValidCompany){
            txtCompany.setStyle(txtCompany.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidName) {
            txtContactPerson.setStyle(txtContactPerson.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidNic) {
            txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if(!isValidAddress){
            txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }


        if (isValidCompany && isValidName && isValidNic && isValidAddress && isValidEmail && isValidPhone) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    supplierId,
                    companyName,
                    contactPerson,
                    nic,
                    address,
                    email,
                    phone
            );

            boolean isUpdated = supplierModel.updateSupplier(supplierDTO);
            if (isUpdated) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update supplier...!").show();
            }
        }
    }

    @FXML
    void generateAllSupplierReportOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        SupplierTM supplierTM = tblSupplier.getSelectionModel().getSelectedItem();
        if(supplierTM != null){
            lblSupplierId.setText(supplierTM.getSupplierId());
            txtCompany.setText(supplierTM.getCompanyName());
            txtContactPerson.setText(supplierTM.getContactPerson());
            txtNic.setText(supplierTM.getNic());
            txtAddress.setText(supplierTM.getAddress());
            txtEmail.setText(supplierTM.getEmail());
            txtPhone.setText(supplierTM.getPhone());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

    @FXML
    void purchaseReportOnAction(ActionEvent event) {

    }

    @FXML
    void resetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void sendMailOnAction(ActionEvent event) {
        SupplierTM selectedItem = tblSupplier.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select customer..!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SendMailView.fxml"));
            Parent load = loader.load();

            SendMailController sendMailController = loader.getController();

            String email = selectedItem.getEmail();
            sendMailController.setRecipientEmail(email);

            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Send Email");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/mail_icon.png")));

            stage.initModality(Modality.APPLICATION_MODAL);

            Window underWindow = btnUpdate.getScene().getWindow();
            stage.initOwner(underWindow);

            stage.showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load UI..!");
            e.printStackTrace();
        }
    }

}
