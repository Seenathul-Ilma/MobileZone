package lk.ijse.gdse71.mobilezone.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse71.mobilezone.dto.*;
import lk.ijse.gdse71.mobilezone.dto.tm.CartTM;
import lk.ijse.gdse71.mobilezone.dto.tm.PurchaseTM;
import lk.ijse.gdse71.mobilezone.model.*;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {

    @FXML
    private ComboBox<String> cmbCategoryId;

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<PurchaseTM, String> colCategoryId;

    @FXML
    private TableColumn<PurchaseTM, String> colItemId;

    @FXML
    private TableColumn<PurchaseTM, String> colItemName;

    @FXML
    private TableColumn<PurchaseTM, Integer> colPurchaseQty;

    @FXML
    private TableColumn<PurchaseTM, Double> colTotal;

    @FXML
    private TableColumn<PurchaseTM, Double> colUnitPrice;

    @FXML
    private TextField txtPurUnitPrice;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblPurchaseDate;

    @FXML
    private Label lblPurchaseId;

    @FXML
    private Label lblQoh;

    @FXML
    private Label lblSupplierName;

    @FXML
    private TableView<PurchaseTM> tblPurchase;

    @FXML
    private TextField txtPurchaseQty;
    private final PurchaseModel purchaseModel = new PurchaseModel();
    private final SupplierModel supplierModel = new SupplierModel();
    private final ItemModel itemModel = new ItemModel();
    private final CategoryModel categoryModel = new CategoryModel();
    private final ObservableList<PurchaseTM> purchaseTMS = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        try {
            refreshPage();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
            e.printStackTrace();
        }
    }

    private void refreshPage() throws SQLException {
        lblPurchaseId.setText(purchaseModel.getNextPurchaseId());

        lblPurchaseDate.setText(LocalDate.now().toString());

        loadSupplierIds();
        loadItemId();
        loadCategoryId();

        cmbSupplierId.getSelectionModel().clearSelection();
        lblSupplierName.setText("");
        cmbCategoryId.getSelectionModel().clearSelection();
        cmbItemId.getSelectionModel().clearSelection();
        lblItemName.setText("");
        lblQoh.setText("");
        txtPurUnitPrice.setText("");
        txtPurchaseQty.setText("");

        purchaseTMS.clear();

        tblPurchase.refresh();
    }

    private void loadCategoryId() throws SQLException {
        ArrayList<String> categoryIds = categoryModel.getAllCategoryIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(categoryIds);
        cmbCategoryId.setItems(observableList);
    }

    private void loadItemId() throws SQLException {
        ArrayList<String> itemIds = itemModel.getAllItemIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemIds);
        cmbItemId.setItems(observableList);
    }

    private void loadSupplierIds() throws SQLException {
        ArrayList<String> supplierIds = supplierModel.getAllSupplierIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(supplierIds);
        cmbSupplierId.setItems(observableList);
    }

    private void setCellValues() {
        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colPurchaseQty.setCellValueFactory(new PropertyValueFactory<>("purchaseQty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("purchaseUnitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("cancelPurchaseBtn"));

        tblPurchase.setItems(purchaseTMS);
    }

    @FXML
    void btnSaveAsOnAction(ActionEvent event) {
        String selectedItemId = cmbItemId.getValue();
        String selectedCategoryId = cmbCategoryId.getValue();

        if (selectedItemId == null) {
            new Alert(Alert.AlertType.ERROR, "Please select an item..!").show();
            return;
        }

        String purchaseQtyString = txtPurchaseQty.getText();
        String price = txtPurUnitPrice.getText();
        String qtyPattern = "^[0-9]+$";
        String pricePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

        if (!purchaseQtyString.matches(qtyPattern)){
            new Alert(Alert.AlertType.ERROR, "Please enter valid quantity..!").show();
            return;
        }

        if(!price.matches(pricePattern)){
            new Alert(Alert.AlertType.ERROR, "Please enter valid price..!").show();
            return;
        }

        String itemName = lblItemName.getText();
        int purQty = Integer.parseInt(purchaseQtyString);
        int qtyOnHand = Integer.parseInt(lblQoh.getText());


        txtPurchaseQty.setText("");

        double unitPrice = Double.parseDouble(txtPurUnitPrice.getText());
        double total = unitPrice * purQty;

        for (PurchaseTM purchaseTM : purchaseTMS) {

            if (purchaseTM.getItemId().equals(selectedItemId)) {
                int newQty = purchaseTM.getPurchaseQty() + purQty;
                purchaseTM.setPurchaseQty(newQty);
                purchaseTM.setTotalAmount(unitPrice * newQty);
                tblPurchase.refresh();
                return;
            }
        }

        Button btn = new Button("Remove");

        PurchaseTM newPurchaseTM = new PurchaseTM(
                selectedCategoryId,
                selectedItemId,
                itemName,
                purQty,
                unitPrice,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {
            purchaseTMS.remove(newPurchaseTM);
            tblPurchase.refresh();
        });

        purchaseTMS.add(newPurchaseTM);
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnProceedSaveOnAction(ActionEvent event) throws SQLException {
        if (tblPurchase.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please add items..!").show();
            return;
        }
        if (cmbSupplierId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select supplier for save your purchase..!").show();
            return;
        }

        double totalAmount = 0;
        String purchaseId = lblPurchaseId.getText();
        Date dateOfPurchase = Date.valueOf(lblPurchaseDate.getText());
        String supplierId = cmbSupplierId.getValue();
        String isReturn = "No";
        ArrayList<PurchaseDetailDTO> purchaseDetailDTOS = new ArrayList<>();

        for (PurchaseTM purchaseTM : purchaseTMS) {
            totalAmount = purchaseTM.getPurchaseUnitPrice()*purchaseTM.getPurchaseQty();

            PurchaseDetailDTO purchaseDetailDTO = new PurchaseDetailDTO(
                    purchaseId,
                    purchaseTM.getItemId(),
                    purchaseTM.getPurchaseQty(),
                    isReturn,
                    purchaseTM.getPurchaseUnitPrice(),
                    totalAmount
            );
            System.out.println("TotalPrice: "+(purchaseTM.getPurchaseUnitPrice()*purchaseTM.getPurchaseQty()));

            purchaseDetailDTOS.add(purchaseDetailDTO);
        }

        PurchaseDTO purchaseDTO = new PurchaseDTO(
                purchaseId,
                supplierId,
                dateOfPurchase,
                totalAmount,
                purchaseDetailDTOS
        );

        boolean isSaved = purchaseModel.savePurchase(purchaseDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Purchase saved..!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save purchase!").show();
        }
    }

    @FXML
    void cmbCategoryOnAction(ActionEvent event) {
        String selectedCategoryId = cmbCategoryId.getSelectionModel().getSelectedItem();
        /*CategoryDTO categoryDTO = categoryModel.findById(selectedCategoryId);

        // If customer found (customerDTO not null)
        if (categoryDTO != null) {
            // FIll customer related labels
            lblCustomerName.setText(customerDTO.getName());
        }*/

    }

    @FXML
    void cmbItemOnAction(ActionEvent event) throws SQLException {
        String selectedItemId = cmbItemId.getSelectionModel().getSelectedItem();
        ItemDTO itemDTO = itemModel.findById(selectedItemId);

        if (itemDTO != null) {
            lblItemName.setText(itemDTO.getName());
            lblQoh.setText(String.valueOf(itemDTO.getQtyOnHand()));
        }
    }

    @FXML
    void cmbSupplierOnAction(ActionEvent event) throws SQLException {
        String selectedSupplierId = cmbSupplierId.getSelectionModel().getSelectedItem();
        SupplierDTO supplierDTO = supplierModel.findById(selectedSupplierId);

        if (supplierDTO != null) {
            lblSupplierName.setText(supplierDTO.getContactPerson());
        }
    }
}
