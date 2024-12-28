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
import lk.ijse.gdse71.mobilezone.dto.CustomerDTO;
import lk.ijse.gdse71.mobilezone.dto.EmployeeDTO;
import lk.ijse.gdse71.mobilezone.dto.tm.CustomerTM;
import lk.ijse.gdse71.mobilezone.dto.tm.EmployeeTM;
import lk.ijse.gdse71.mobilezone.dto.tm.LogInCredentialTM;
import lk.ijse.gdse71.mobilezone.model.EmployeeModel;
import lk.ijse.gdse71.mobilezone.model.LogInCredentialModel;
import lk.ijse.gdse71.mobilezone.model.OrdersModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    private Connection connection;
    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<EmployeeTM, String> colComp;

    @FXML
    private TableColumn<EmployeeTM, String> colDesignation;

    @FXML
    private TableColumn<EmployeeTM, String> colEmail;

    @FXML
    private TableColumn<EmployeeTM, String> colEmployeeId;

    @FXML
    private TableColumn<EmployeeTM, String> colName;

    @FXML
    private TableColumn<EmployeeTM, String> colNic;

    @FXML
    private TableColumn<EmployeeTM, String> colPhone;

    @FXML
    private TableColumn<EmployeeTM, String> colSalary;
    @FXML
    private TableColumn<EmployeeTM, String> colTotalSalary;

    @FXML
    private TableColumn<EmployeeTM, String> colUserId;

    @FXML
    private ComboBox<String> comboBoxUserId;

    @FXML
    private Label lblEmployeeId;

    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TextField txtComp;

    @FXML
    private TextField txtDesignation;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNic;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSalary;

    private final LogInCredentialModel logInCredentialModel = new LogInCredentialModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colComp.setCellValueFactory(new PropertyValueFactory<>("compensation"));
        colTotalSalary.setCellValueFactory(new PropertyValueFactory<>("totalSalary"));
        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load employee id").show();
        }
    }

    private void refreshPage() throws SQLException {
        loadNextEmployeeId();
        loadTableData();
        loadUserIds();
        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        comboBoxUserId.getSelectionModel().clearSelection();
        txtName.setText("");
        txtNic.setText("");
        txtDesignation.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtSalary.setText("");
        txtComp.setText("");
    }

    EmployeeModel employeeModel = new EmployeeModel();
    private void loadTableData() throws SQLException {
        ArrayList<EmployeeDTO> employeeDTOS = employeeModel.getAllEmployees();
        ObservableList<EmployeeTM> employeeTMS = FXCollections.observableArrayList();

        for(EmployeeDTO employeeDTO : employeeDTOS){
            EmployeeTM employeeTM = new EmployeeTM(
                    employeeDTO.getEmployeeId(),
                    employeeDTO.getUserId(),
                    employeeDTO.getName(),
                    employeeDTO.getNic(),
                    employeeDTO.getDesignation(),
                    employeeDTO.getEmail(),
                    employeeDTO.getContact(),
                    employeeDTO.getSalary(),
                    employeeDTO.getCompensation(),
                    employeeDTO.getTotalSalary()
            );
            employeeTMS.add(employeeTM);
        }
        tblEmployee.setItems(employeeTMS);
    }

    private void loadNextEmployeeId() throws SQLException {
        String nextEmployeeId = employeeModel.getNextEmployeeId();
        lblEmployeeId.setText(nextEmployeeId);
    }

    @FXML
    void SalaryReportOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String employeeId = lblEmployeeId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this employee?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if(optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES){
            boolean isDeleted = employeeModel.deleteEmployee(employeeId);
            if(isDeleted){
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();
            } else{
                new Alert(Alert.AlertType.ERROR, "Failed to delete employee!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String selectedUserId = comboBoxUserId.getValue();
        String employeeId = lblEmployeeId.getText();
        //String userId = txtUserId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String designation = txtDesignation.getText();
        String email = txtEmail.getText();
        String contact = txtPhone.getText();
        String salary = txtSalary.getText();
        String compensation = txtComp.getText();
        String totalSalary = "";

        //txtUserId.setStyle(txtUserId.getStyle() + ";-fx-border-color: #7367F0;");
        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");
        txtDesignation.setStyle(txtDesignation.getStyle() + ";-fx-border-color: #7367F0;");
        txtSalary.setStyle(txtSalary.getStyle() + ";-fx-border-color: #7367F0;");
        txtComp.setStyle(txtComp.getStyle() + ";-fx-border-color: #7367F0;");

        //String userIdPattern = "^U\\d{3}$";
        String namePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String designationPattern = "^[A-Za-z ]+$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
        String salaryPattern = "^([1-9]\\d{0,5}|0)(\\.\\d{1,2})?$";
        //String compPattern = "^[+-]?(?:0|[1-9]\\d*)(?:\\.\\d+)?$";
        String compPattern = "^-?\\d+(\\.\\d{1,2})?$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidDesignation = designation.matches(designationPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = contact.matches(phonePattern);
        boolean isValidSalary = salary.matches(salaryPattern);
        boolean isValidComp = compensation.matches(compPattern);

        boolean isDuplicateUserId = employeeModel.checkUserIds(selectedUserId);

        if (selectedUserId == null ) {
            new Alert(Alert.AlertType.ERROR, "Please select a user id..!").show();
            return;
        }else if(isDuplicateUserId){
            new Alert(Alert.AlertType.ERROR, "Selected User Id is duplicated! Please select another one!").show();
            return;
        }

        if (!isValidName) {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidNic) {
            txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidDesignation) {
            txtDesignation.setStyle(txtDesignation.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidSalary) {
            txtSalary.setStyle(txtSalary.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidComp || Double.valueOf(compensation) < -100 || Double.valueOf(compensation) > 100) {
            txtComp.setStyle(txtComp.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Compensation!");
            return;
        }else{
            totalSalary = calculateSalary(salary, compensation);
        }


        if (selectedUserId != null && isValidName && isValidNic && isValidDesignation && isValidEmail && isValidPhone && isValidSalary && isValidComp) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    employeeId,
                    selectedUserId,
                    name,
                    nic,
                    designation,
                    email,
                    contact,
                    salary,
                    compensation,
                    totalSalary
            );

            boolean isSaved = employeeModel.saveEmployee(employeeDTO);
            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee saved...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save employee...!").show();
            }
        }

    }

    private String calculateSalary(String salary, String compensation) {
        double compensationPercent = Double.parseDouble(compensation);
        double baseSalary = Double.parseDouble(salary);

        double adjustment = baseSalary * Math.abs(compensationPercent) / 100;

        double totalSalary;
        if (compensationPercent > 0) {
            totalSalary = baseSalary + adjustment;
            return String.valueOf(totalSalary);
        } else if(compensationPercent < 0){
            totalSalary = baseSalary - adjustment;
            return String.valueOf(totalSalary);
        }else if(compensation.isEmpty() || Double.valueOf(compensation) == 0 || compensation.equals("")){
            return salary;
        }
        return salary;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String selectedUserId = comboBoxUserId.getValue();
        String employeeId = lblEmployeeId.getText();
        //String userId = txtUserId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String designation = txtDesignation.getText();
        String email = txtEmail.getText();
        String contact = txtPhone.getText();
        String salary = txtSalary.getText();
        String compensation = txtComp.getText();
        String totalSalary = "";

        //txtUserId.setStyle(txtUserId.getStyle() + ";-fx-border-color: #7367F0;");
        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");
        txtDesignation.setStyle(txtDesignation.getStyle() + ";-fx-border-color: #7367F0;");
        txtSalary.setStyle(txtSalary.getStyle() + ";-fx-border-color: #7367F0;");
        txtComp.setStyle(txtComp.getStyle() + ";-fx-border-color: #7367F0;");

        //String userIdPattern = "^U\\d{3}$";
        String namePattern = "^[A-Za-z ]+$";
        String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
        String designationPattern = "^[A-Za-z ]+$";
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
        String salaryPattern = "^([1-9]\\d{0,5}|0)(\\.\\d{1,2})?$";
        //String compPattern = "^[+-]?(?:0|[1-9]\\d*)(?:\\.\\d+)?$";
        String compPattern = "^-?\\d+(\\.\\d{1,2})?$";

        boolean isValidName = name.matches(namePattern);
        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidDesignation = designation.matches(designationPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = contact.matches(phonePattern);
        boolean isValidSalary = salary.matches(salaryPattern);
        boolean isValidComp = compensation.matches(compPattern);

        boolean isDuplicateUserId = employeeModel.checkUserIds(selectedUserId, 0);

        if (selectedUserId == null ) {
            new Alert(Alert.AlertType.ERROR, "Please select a user id..!").show();
            return;
        }else if(isDuplicateUserId){
            new Alert(Alert.AlertType.ERROR, "Selected User Id is duplicated! Please select another one!").show();
            return;
        }

        if (!isValidName) {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidNic) {
            txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidDesignation) {
            txtDesignation.setStyle(txtDesignation.getStyle() + ";-fx-border-color: red;");
            return;
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidSalary) {
            txtSalary.setStyle(txtSalary.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidComp || Double.valueOf(compensation) < -100 || Double.valueOf(compensation) > 100) {
            txtComp.setStyle(txtComp.getStyle() + ";-fx-border-color: red;");
            System.out.println("Invalid Compensation!");
            return;
        }else{
            totalSalary = calculateSalary(salary, compensation);
        }


        if (selectedUserId != null && isValidName && isValidNic && isValidDesignation && isValidEmail && isValidPhone && isValidSalary && isValidComp) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    employeeId,
                    selectedUserId,
                    name,
                    nic,
                    designation,
                    email,
                    contact,
                    salary,
                    compensation,
                    totalSalary
            );

            boolean isUpdated = employeeModel.updateEmployee(employeeDTO);
            if (isUpdated) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Employee updated...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update employee...!").show();
            }
        }

    }

    @FXML
    void compensationOnClick(MouseEvent event) {
        //String compen = compComboBox.getValue();
        /*if((compComboBox.getValue()).equals("Not Available")){
            txtComp.setDisable(true);
            calculateSalary(txtSalary.getText(), "0");
            colComp.setText("-");
        }else if((compComboBox.getValue()).equals("Bonus")){
            calculateSalary(txtSalary.getText(), txtComp.getText());
        }else if((compComboBox.getValue()).equals("Raise")){
            String newSalary = calculateSalary(txtSalary.getText(), txtComp.getText());
            double salaryAfterRaise = Double.parseDouble(txtSalary.getText() + newSalary);
            colSalary.setText(String.valueOf(salaryAfterRaise));
        }else if((compComboBox.getValue()).equals("Decrement")){
            String newSalary = calculateSalary(txtSalary.getText(), txtComp.getText());
            double salaryAfterDecrement = (Double.parseDouble(txtSalary.getText())-Double.parseDouble(newSalary));
            colSalary.setText(String.valueOf(salaryAfterDecrement));
        }*/
    }

    private void loadUserIds() throws SQLException {
        ArrayList<String> userIds = logInCredentialModel.getAllUserIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(userIds);
        comboBoxUserId.setItems(observableList);
    }

    @FXML
    void generateAllEmployeeReportOnAction(ActionEvent event) {

    }

    @FXML
    void onClickTable(MouseEvent event) {
        EmployeeTM employeeTM = tblEmployee.getSelectionModel().getSelectedItem();
        if(employeeTM != null){
            lblEmployeeId.setText(employeeTM.getEmployeeId());

            comboBoxUserId.setValue(employeeTM.getUserId());
            //txtUserId.setText(employeeTM.getUserId());
            txtName.setText(employeeTM.getName());
            txtNic.setText(employeeTM.getNic());
            txtDesignation.setText(employeeTM.getDesignation());
            txtEmail.setText(employeeTM.getEmail());
            txtPhone.setText(employeeTM.getContact());
            txtSalary.setText(employeeTM.getSalary());
            txtComp.setText(employeeTM.getCompensation());

            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

    @FXML
    void sendMailOnAction(ActionEvent event) {
        EmployeeTM selectedItem = tblEmployee.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an employee..!");
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
