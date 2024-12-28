package lk.ijse.gdse71.mobilezone.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse71.mobilezone.dto.CategoryDTO;
import lk.ijse.gdse71.mobilezone.dto.ExpenseDTO;
import lk.ijse.gdse71.mobilezone.dto.tm.CategoryTM;
import lk.ijse.gdse71.mobilezone.dto.tm.ExpenseTM;
import lk.ijse.gdse71.mobilezone.model.ExpenseModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ExpenseController implements Initializable{

    @FXML private Button btnDelete;
    @FXML private Button btnReset;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<ExpenseTM, Double> colAmount;
    @FXML private TableColumn<ExpenseTM, String> colDesc;
    @FXML private TableColumn<ExpenseTM, Date> colExpDate;
    @FXML private TableColumn<ExpenseTM, String> colExpId;
    @FXML private TableColumn<ExpenseTM, String> colExpCategory;
    @FXML private Label lblDate;
    @FXML private TableView<ExpenseTM> tblExpenses;
    @FXML private TextField txtAmount;
    @FXML private TextField txtExpCategory;
    @FXML private DatePicker txtDateOfExpense;
    @FXML private TextField txtDescription;
    @FXML private Label lblExpId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colExpId.setCellValueFactory(new PropertyValueFactory<>("exp_Id"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colExpCategory.setCellValueFactory(new PropertyValueFactory<>("expCategory"));
        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Expense Id").show();
        }
    }

    private void refreshPage() throws SQLException {
        loadNextExpenseId();
        loadTableData();
        lblDate.setText(LocalDate.now().toString());
        btnSave.setDisable(false);

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnReset.setDisable(false);
        txtDescription.setText("");
        txtAmount.setText("");
        txtExpCategory.setText("");
        txtDateOfExpense.setValue(null);
    }

    ExpenseModel expenseModel = new ExpenseModel();
    private void loadTableData() throws SQLException {
        ArrayList<ExpenseDTO> expenseDTOS = expenseModel.getAllExpenses();
        ObservableList<ExpenseTM> expenseTMS = FXCollections.observableArrayList();

        for(ExpenseDTO expenseDTO : expenseDTOS){
            ExpenseTM expenseTM = new ExpenseTM(
                    expenseDTO.getExp_Id(),
                    expenseDTO.getDescription(),
                    expenseDTO.getAmount(),
                    expenseDTO.getDate(),
                    expenseDTO.getExpCategory()
            );
            expenseTMS.add(expenseTM);
        }
        tblExpenses.setItems(expenseTMS);
    }

    private void loadNextExpenseId() throws SQLException {
        String nextExpenseId = expenseModel.getNextExpenseId();
        lblExpId.setText(nextExpenseId);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String expId = lblExpId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = expenseModel.deleteExpense(expId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Expense record deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete expense...!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void datePickerOnAction(ActionEvent event) {
        LocalDate selectedDate = txtDateOfExpense.getValue();
        LocalDate today = LocalDate.now();
        String dateString = String.valueOf(selectedDate);
        if (selectedDate != null) {
            if (selectedDate.isAfter(today)) {
                txtDateOfExpense.setValue(null); // Reset selection
                new Alert(Alert.AlertType.ERROR, "The selected date is in the future and not allowed!\nPlease input a valid date!").show();
                return;
            } else if (selectedDate.getMonth() != today.getMonth() || selectedDate.getYear() != today.getYear()) {
                txtDateOfExpense.setValue(null); // Reset selection
                new Alert(Alert.AlertType.ERROR, "The selected date is not in the current month!\nPlease input a valid date!").show();
                return;
            }
        }

        /*try {
            Date date = Date.valueOf(dateString);
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please enter a valid date (yyyy-MM-dd).").show();
            return;
        }*/

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String expId = lblExpId.getText();
        String desc = txtDescription.getText();
        String category = txtExpCategory.getText();
        String dateString = String.valueOf(txtDateOfExpense.getValue());
        /*if(txtExpCategory.getText().isEmpty() || txtDateOfExpense.getValue() == null || txtAmount.getText().isEmpty() || txtDescription.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }*/
        try {
            Date date = Date.valueOf(dateString);
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please enter a valid date (yyyy-MM-dd).").show();
            return;
        }

        if(desc.equals("") || txtDateOfExpense.getValue() == null|| txtAmount.getText().equals("") || category.equals("")){
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        double amount = Double.parseDouble(txtAmount.getText());
        Date date = Date.valueOf(txtDateOfExpense.getValue());

        ExpenseDTO expenseDTO = new ExpenseDTO(
                expId,
                desc,
                amount,
                date,
                category
        );

        boolean isSaved = expenseModel.saveExpense(expenseDTO);
        if (isSaved) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Expense saved successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save expense...!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws SQLException {
        String expId = lblExpId.getText();
        String desc = txtDescription.getText();
        String category = txtExpCategory.getText();
        String dateString = String.valueOf(txtDateOfExpense.getValue());
        LocalDate selectedDate = txtDateOfExpense.getValue();
        Date date = Date.valueOf(txtDateOfExpense.getValue());

        try {
            Date expDate = Date.valueOf(dateString);
            if(selectedDate == null || dateString.equals("") || date == null){
                new Alert(Alert.AlertType.ERROR, "Please fill date field!").show();
                return;
            }
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please enter a valid date (yyyy-MM-dd).").show();
            return;
        }

        if(desc.equals("") || txtAmount.getText().equals("") || category==null){
            new Alert(Alert.AlertType.ERROR, "Above fields cannot be empty! Fill those empty fields.. ").show();
            return;
        }

        double amount = Double.parseDouble(txtAmount.getText());

        ExpenseDTO expenseDTO = new ExpenseDTO(
                expId,
                desc,
                amount,
                date,
                category
        );
        boolean isUpdated = expenseModel.updateExpense(expenseDTO);
        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Expense updated successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update expense...!").show();
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        ExpenseTM expenseTM = tblExpenses.getSelectionModel().getSelectedItem();
        if (expenseTM != null) {
            lblExpId.setText(expenseTM.getExp_Id());
            txtAmount.setText(String.valueOf(expenseTM.getAmount()));
            txtDateOfExpense.setValue(expenseTM.getDate().toLocalDate());
            txtDescription.setText(expenseTM.getDescription());
            txtExpCategory.setText(expenseTM.getExpCategory());
            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
        }
    }

}
