package lk.ijse.gdse71.mobilezone.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse71.mobilezone.dto.CustomerDTO;
import lk.ijse.gdse71.mobilezone.dto.ItemDTO;
import lk.ijse.gdse71.mobilezone.dto.tm.CustomerTM;
import lk.ijse.gdse71.mobilezone.dto.tm.ItemTM;
import lk.ijse.gdse71.mobilezone.model.ItemModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController implements Initializable {

    @FXML
    private Button btnDeleteItem;

    @FXML
    private Button btnCheckLowStock;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSaveItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private TableColumn<ItemTM, String> colBrand;
    @FXML
    private TableColumn<ItemTM, String> colCategoryId;

    @FXML
    private TableColumn<ItemTM, String> colItemId;

    @FXML
    private TableColumn<ItemTM, String> colModel;

    @FXML
    private TableColumn<ItemTM, String> colName;

    @FXML
    private TableColumn<ItemTM, Integer> colReOrderLevel;

    @FXML
    private TableColumn<ItemTM, Double> colPrice;

    @FXML
    private TableColumn<ItemTM, Integer> colQty;

    @FXML
    private Label lblItemId;
    @FXML
    private TableView<ItemTM> tblItem;
    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtCategoryId;

    @FXML
    private TextField txtModel;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtReOrderLevel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colReOrderLevel.setCellValueFactory(new PropertyValueFactory<>("reOrderLevel"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load item id").show();
        }
    }

    private void refreshPage() throws SQLException {
        loadNextItemId();
        loadTableData();

        btnSaveItem.setDisable(false);

        btnUpdateItem.setDisable(true);
        btnDeleteItem.setDisable(true);
        boolean isEmpty = itemModel.isEmpty();
        if(isEmpty){
            btnCheckLowStock.setDisable(true);
        }else{
            btnCheckLowStock.setDisable(false);
        }

        txtCategoryId.setText("");
        txtName.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtQuantity.setText("");
        txtReOrderLevel.setText("");
        txtPrice.setText("");
    }

    ItemModel itemModel = new ItemModel();
    private void loadTableData() throws SQLException {
        ArrayList<ItemDTO> itemDTOS = itemModel.getAllItems();
        ObservableList<ItemTM> itemTMS = FXCollections.observableArrayList();

        for(ItemDTO itemDTO : itemDTOS){
            ItemTM itemTM = new ItemTM(
                    itemDTO.getItemId(),
                    itemDTO.getCategoryId(),
                    itemDTO.getName(),
                    itemDTO.getBrand(),
                    itemDTO.getModel(),
                    itemDTO.getQtyOnHand(),
                    itemDTO.getReOrderLevel(),
                    itemDTO.getUnitPrice()
            );
            itemTMS.add(itemTM);
        }
        tblItem.setItems(itemTMS);
    }

    private void loadNextItemId() throws SQLException {
        String nextItemId = itemModel.getNextItemId();
        lblItemId.setText(nextItemId);
    }


    @FXML
    public void btnDeleteItemOnAction(ActionEvent event) throws SQLException {
        String itemId = lblItemId.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.isPresent() && optionalButtonType.get() == ButtonType.YES) {

            boolean isDeleted = itemModel.deleteItem(itemId);
            if (isDeleted) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Item deleted...!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to delete item...!").show();
            }
        }

    }

    @FXML
    public void btnResetOnAction(ActionEvent event) throws SQLException {
        refreshPage();
    }

    @FXML
    public void btnSaveItemOnAction(ActionEvent event) throws SQLException {
        String itemId = lblItemId.getText();
        String categoryId = txtCategoryId.getText();
        String name = txtName.getText();
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        int qty = Math.abs(Integer.parseInt(txtQuantity.getText()));
        int reOrderLevel = Math.abs(Integer.parseInt(txtReOrderLevel.getText()));
        double price = Math.abs(Double.valueOf(txtPrice.getText()));

        txtCategoryId.setStyle(txtCategoryId.getStyle() + ";-fx-border-color: #7367F0;");
        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtBrand.setStyle(txtBrand.getStyle() + ";-fx-border-color: #7367F0;");
        txtModel.setStyle(txtModel.getStyle() + ";-fx-border-color: #7367F0;");
        txtQuantity.setStyle(txtQuantity.getStyle() + ";-fx-border-color: #7367F0;");
        txtReOrderLevel.setStyle(txtReOrderLevel.getStyle() + ";-fx-border-color: #7367F0;");
        txtPrice.setStyle(txtPrice.getStyle() + ";-fx-border-color: #7367F0;");

        ItemDTO itemDTO = new ItemDTO(
                itemId,
                categoryId,
                name,
                brand,
                model,
                qty,
                reOrderLevel,
                price
        );

        boolean isSaved = itemModel.saveItem(itemDTO);
        if (isSaved) {
            refreshPage();
            System.out.println("Item save: "+qty);
            new Alert(Alert.AlertType.INFORMATION, "Item saved successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save item...!").show();
        }
    }

    @FXML
    public void btnUpdateItemOnAction(ActionEvent event) throws SQLException {
        String itemId = lblItemId.getText();
        String categoryId = txtCategoryId.getText();
        String name = txtName.getText();
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        int qty = Integer.parseInt(txtQuantity.getText());
        int reOrderLevel = Integer.parseInt(txtReOrderLevel.getText());
        double price = Double.parseDouble(txtPrice.getText());

        txtCategoryId.setStyle(txtCategoryId.getStyle() + ";-fx-border-color: #7367F0;");
        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtBrand.setStyle(txtBrand.getStyle() + ";-fx-border-color: #7367F0;");
        txtModel.setStyle(txtModel.getStyle() + ";-fx-border-color: #7367F0;");
        txtQuantity.setStyle(txtQuantity.getStyle() + ";-fx-border-color: #7367F0;");
        txtReOrderLevel.setStyle(txtReOrderLevel.getStyle() + ";-fx-border-color: #7367F0;");
        txtPrice.setStyle(txtPrice.getStyle() + ";-fx-border-color: #7367F0;");

        if (qty < 0) {
            qty = Math.abs(qty);
        }else if(reOrderLevel < 0){
            reOrderLevel = Math.abs(reOrderLevel);
        }else if(price < 0){
            price = Math.abs(price);
        }

        ItemDTO itemDTO = new ItemDTO(
                itemId,
                categoryId,
                name,
                brand,
                model,
                qty,
                reOrderLevel,
                price
        );

        boolean isUpdated = itemModel.updateItem(itemDTO);
        if (isUpdated) {
            refreshPage();
            new Alert(Alert.AlertType.INFORMATION, "Item updated successfully!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to update item...!").show();
        }
    }

    @FXML
    public void checkLowStockItemsOnAction(ActionEvent event) throws SQLException {
        ArrayList<ItemDTO> itemDTOS = itemModel.checkLowStockItems();
        ObservableList<ItemTM> itemTMS = FXCollections.observableArrayList();

        for(ItemDTO itemDTO : itemDTOS){
            ItemTM itemTM = new ItemTM(
                    itemDTO.getItemId(),
                    itemDTO.getCategoryId(),
                    itemDTO.getName(),
                    itemDTO.getBrand(),
                    itemDTO.getModel(),
                    itemDTO.getQtyOnHand(),
                    itemDTO.getReOrderLevel(),
                    itemDTO.getUnitPrice()
            );
            itemTMS.add(itemTM);
        }
        tblItem.setItems(itemTMS);
    }

    @FXML
    public void onClickTable(MouseEvent event) {
        ItemTM itemTM = tblItem.getSelectionModel().getSelectedItem();
        if (itemTM != null) {
            lblItemId.setText(itemTM.getItemId());
            txtCategoryId.setText(itemTM.getCategoryId());
            txtName.setText(itemTM.getName());
            txtBrand.setText(itemTM.getBrand());
            txtModel.setText(itemTM.getModel());
            txtQuantity.setText(String.valueOf(itemTM.getQtyOnHand()));
            txtReOrderLevel.setText(String.valueOf(itemTM.getReOrderLevel()));
            txtPrice.setText(String.valueOf(itemTM.getUnitPrice()));

            btnSaveItem.setDisable(true);

            btnDeleteItem.setDisable(false);
            btnUpdateItem.setDisable(false);
        }
    }
}
