package lk.ijse.gdse71.mobilezone.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.gdse71.mobilezone.dto.*;
import lk.ijse.gdse71.mobilezone.dto.tm.PurchaseReturnTM;
import lk.ijse.gdse71.mobilezone.dto.tm.ReturnTM;
import lk.ijse.gdse71.mobilezone.model.*;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PurchaseReturnController implements Initializable {

    @FXML
    private ComboBox<String> cmbItemId;

    @FXML
    private ComboBox<String> cmbPurchaseId;

    @FXML
    private ComboBox<String> cmbReason;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private ComboBox<String> cmbCategoryId;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<PurchaseReturnTM, String> colCategoryId;

    @FXML
    private TableColumn<PurchaseReturnTM, String> colItemId;

    @FXML
    private TableColumn<PurchaseReturnTM, String> colItemName;

    @FXML
    private TableColumn<PurchaseReturnTM, Integer> colRetQty;

    @FXML
    private TableColumn<PurchaseReturnTM, Double> colTotal;

    @FXML
    private TableColumn<PurchaseReturnTM, Double> colUnitPrice;

    @FXML
    private Label lblBoughtPrice;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblPurchaseDate;

    @FXML
    private Label lblPurchaseRetId;

    @FXML
    private Label lblQoh;

    @FXML
    private Label lblReturnDate;

    @FXML
    private Label lblRtnQty;

    @FXML
    private Label lblSupplierName;

    @FXML
    private TableView<PurchaseReturnTM> tblPurchaseReturn;

    private final PurchaseReturnModel purchaseReturnModel = new PurchaseReturnModel();
    private final PurchaseModel purchaseModel = new PurchaseModel();

    private final PurchaseDetailModel purchaseDetailModel = new PurchaseDetailModel();
    private final SupplierModel supplierModel = new SupplierModel();
    private final CategoryModel categoryModel = new CategoryModel();
    private final ItemModel itemModel = new ItemModel();

    private final ObservableList<PurchaseReturnTM> purchaseReturnTMS = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();
        try {
            refreshPage();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }
    }
    private void setCellValues() {
        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colRetQty.setCellValueFactory(new PropertyValueFactory<>("returnQty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("cancelPurchaseReturnBtn"));

        tblPurchaseReturn.setItems(purchaseReturnTMS);
    }

    private void refreshPage() throws SQLException {
        lblPurchaseRetId.setText(purchaseReturnModel.getNextPurchaseReturnId());

        lblReturnDate.setText(LocalDate.now().toString());

        loadPurchaseId();
        loadSupplierIds();
        loadCategoryId();
        loadItemId();
        loadReasons();

        cmbPurchaseId.getSelectionModel().clearSelection();
        lblPurchaseDate.setText("");
        cmbSupplierId.getSelectionModel().clearSelection();
        lblSupplierName.setText("");
        cmbCategoryId.getSelectionModel().clearSelection();
        cmbItemId.getSelectionModel().clearSelection();
        lblItemName.setText("");
        lblQoh.setText("");
        lblBoughtPrice.setText("");
        lblRtnQty.setText("");
        cmbReason.getSelectionModel().clearSelection();

        purchaseReturnTMS.clear();

        tblPurchaseReturn.refresh();
    }

    private void loadReasons() {
        ObservableList<String> reasons = FXCollections.observableArrayList(
                "Defective Product",
                "Quality Issues",
                "End Of Life",
                "Change of Mind",
                "Size or Fit Issue",
                "Product Performance Issue",
                "Misleading Description",
                "Color/Model Mismatch",
                "Received Extra Items",
                "Damaged Packaging",
                "Component Wear and Tear",
                "Price Issues Or Better Price Available",
                "Incompatibility Or Obsolescence",
                "Environmental Factors",
                "Warranty Claim",
                "Wrong Quantity Ordered",
                "Product No Longer Needed"
        );
        cmbReason.setItems(reasons);
    }

    private void loadItemId() throws SQLException {
        ArrayList<String> itemIds = itemModel.getAllItemIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(itemIds);
        cmbItemId.setItems(observableList);
    }

    private void loadCategoryId() throws SQLException {
        ArrayList<String> categoryIds = categoryModel.getAllCategoryIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(categoryIds);
        cmbCategoryId.setItems(observableList);
    }

    private void loadSupplierIds() throws SQLException {
        ArrayList<String> supplierIds = supplierModel.getAllSupplierIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(supplierIds);
        cmbSupplierId.setItems(observableList);
    }

    private void loadPurchaseId() throws SQLException {
        ArrayList<String> purchaseIds = purchaseModel.getAllPurchaseIds();
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(purchaseIds);
        cmbPurchaseId.setItems(observableList);
    }

    @FXML
    void cmbCategoryOnAction(ActionEvent event){
        String selectedCategoryId = cmbCategoryId.getValue();
    }

    @FXML
    void cmbItemOnAction(ActionEvent event) throws SQLException {
        String selectedItemId = cmbItemId.getSelectionModel().getSelectedItem();
        if (selectedItemId == null) {
            return; // Do nothing if no item is selected
        }
        String purchaseId = cmbPurchaseId.getValue();
        String categoryId = cmbCategoryId.getValue();
        ItemDTO itemDTO = itemModel.findById(selectedItemId);
        String confirmCategory = itemModel.confirmItemCategory(selectedItemId);
        if(!confirmCategory.equals(categoryId)){
            new Alert(Alert.AlertType.ERROR, "Item didn't belongs to categoryId! ").show();
            return;
        }
        PurchaseDetailDTO purchaseDetailDTO = purchaseDetailModel.findById(purchaseId, selectedItemId);
        String AlreadyFullyReturned = purchaseDetailModel.getReturnOrNot(purchaseId, selectedItemId);
        // If item found (itemDTO not null)
        if (itemDTO != null && purchaseDetailDTO != null) {
            // FIll item related labels
            lblItemName.setText(itemDTO.getName());
            lblQoh.setText(String.valueOf(itemDTO.getQtyOnHand()));
            lblRtnQty.setText(String.valueOf(purchaseDetailDTO.getQuantity()));
            lblBoughtPrice.setText(String.valueOf(purchaseDetailDTO.getUnitPrice()));
        }
        if(AlreadyFullyReturned.equals("YES")){
            lblRtnQty.setText("0");
        }
    }

    @FXML
    void cmbPurchaseOnAction(ActionEvent event) {
        String selectedPurchaseId = cmbPurchaseId.getSelectionModel().getSelectedItem();
    }

    @FXML
    void cmbReasonOnAction(ActionEvent event) {
        String selectedReason = cmbReason.getSelectionModel().getSelectedItem();
    }

    @FXML
    void cmbSupplierOnAction(ActionEvent event) throws SQLException {
        String selectedSupplierId = cmbSupplierId.getSelectionModel().getSelectedItem();
        String purchaseId = cmbPurchaseId.getValue();
        SupplierDTO supplierDTO = supplierModel.findById(selectedSupplierId);
        PurchaseDTO purchaseDTO = purchaseModel.checkById(purchaseId);
        // If supplier found (supplierDTO not null)
        if (supplierDTO != null && purchaseDTO != null) {
            // FIll supplier related labels
            lblSupplierName.setText(supplierDTO.getContactPerson());
            lblPurchaseDate.setText(String.valueOf(purchaseDTO.getPurchaseDate()));
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    void btnReturnToSupplierOnAction(ActionEvent event) throws SQLException {
        String selectedItemId = cmbItemId.getValue();
        String selectedPurchaseId = cmbPurchaseId.getValue();
        String selectedReason = cmbReason.getValue();
        String selectedSupplierId = cmbSupplierId.getValue();
        String selectedCategoryId = cmbCategoryId.getValue();

        int rtnQty = 0;
        double boughtPrice = 0, total = 0;
        String confirmItem = purchaseDetailModel.confirmItemBought(selectedPurchaseId, selectedItemId);
        String confirmSupplier = purchaseDetailModel.confirmBoughtFromSupplier(selectedSupplierId, selectedPurchaseId, selectedItemId);
        if(confirmItem.equals(selectedItemId)){
            rtnQty = Integer.parseInt(lblRtnQty.getText());
            boughtPrice = Double.parseDouble(lblBoughtPrice.getText());
            total = rtnQty * boughtPrice;
        }else{
            new Alert(Alert.AlertType.ERROR, "This purchase didn't contain this item!").show();
            return;
        }
        if(!confirmSupplier.equals(selectedSupplierId)){
            new Alert(Alert.AlertType.ERROR, "Oops! Supplier mismatch.. This purchase didn't contain this item!").show();
            return;
        }

        String AlreadyFullyReturned = purchaseDetailModel.getReturnOrNot(selectedPurchaseId, selectedItemId);
        if(AlreadyFullyReturned.equals("YES")){
            new Alert(Alert.AlertType.ERROR, "Item Already fully return in this purchaseId..!").show();
            refreshPage();
            return;
        }

        if (cmbPurchaseId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You should select the purchaseId first..!").show();
            return;
        }else if(cmbItemId.getSelectionModel().isEmpty() || cmbReason.getSelectionModel().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "You should select item and returning reason..!").show();
            return;
        }else if(selectedPurchaseId == null){
            new Alert(Alert.AlertType.ERROR, "Oops.. No such a purchase!").show();
        }

        String itemName = lblItemName.getText();

        Button btn = new Button("Remove");

        PurchaseReturnTM newPurchaseReturnTM = new PurchaseReturnTM(
                selectedCategoryId,
                selectedItemId,
                itemName,
                rtnQty,
                boughtPrice,
                total,
                btn
        );

        btn.setOnAction(actionEvent -> {
            purchaseReturnTMS.remove(newPurchaseReturnTM);
            tblPurchaseReturn.refresh();
        });

        purchaseReturnTMS.add(newPurchaseReturnTM);
    }

    @FXML
    void btnProceedReturnOnAction(ActionEvent event) throws SQLException {
        if (tblPurchaseReturn.getItems().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "OOps.. You can't return empty values!").show();
            return;
        }
        if (cmbPurchaseId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select purchase to return..!").show();
            return;
        }

        String purchaseRetId = lblPurchaseRetId.getText();
        String purchaseId = cmbPurchaseId.getValue();
        String supplierId = cmbSupplierId.getValue();
        String categoryId = cmbCategoryId.getValue();
        String itemId = cmbItemId.getValue();
        String reason = cmbReason.getValue();
        Date dateOfPurchase = Date.valueOf(lblPurchaseDate.getText());
        Date dateOfReturn = Date.valueOf(lblReturnDate.getText());
        int qtyOnHand = Integer.parseInt(lblQoh.getText());
        double boughtPrice = Double.parseDouble(lblBoughtPrice.getText());
        int returnQty = Integer.parseInt(lblRtnQty.getText());
        double totalAmount = boughtPrice * returnQty;
        String isReturned = "YES";

        ArrayList<PurchaseDetailDTO> purchaseDetailDTOS = new ArrayList<>();

        for (PurchaseReturnTM purchaseReturnTM : purchaseReturnTMS) {

            PurchaseDetailDTO purchaseDetailDTO = new PurchaseDetailDTO(
                    purchaseId,
                    purchaseReturnTM.getItemId(),
                    purchaseReturnTM.getReturnQty(),
                    isReturned,
                    purchaseReturnTM.getUnitPrice(),
                    purchaseReturnTM.getTotalAmount()
            );
            purchaseDetailDTOS.add(purchaseDetailDTO);
        }

        PurchaseReturnDTO purchaseReturnDTO = new PurchaseReturnDTO(
                purchaseRetId,
                purchaseId,
                supplierId,
                itemId,
                returnQty,
                reason,
                totalAmount,
                dateOfReturn,
                purchaseDetailDTOS
        );


        boolean isSaved = purchaseReturnModel.saveReturn(purchaseReturnDTO);

        if (isSaved) {
            new Alert(Alert.AlertType.INFORMATION, "Purchase Returned..!").show();
            refreshPage();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to return purchase..!").show();
        }
    }
}
