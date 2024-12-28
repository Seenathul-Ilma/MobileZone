package lk.ijse.gdse71.mobilezone.model;
import lk.ijse.gdse71.mobilezone.dto.ItemDTO;
import lk.ijse.gdse71.mobilezone.dto.OrderDetailDTO;
import lk.ijse.gdse71.mobilezone.dto.PurchaseDetailDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemModel {

    public ArrayList<ItemDTO> getAllItems() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from item");

        ArrayList<ItemDTO> itemDTOS = new ArrayList<>();

        while (rst.next()) {
            ItemDTO itemDTO = new ItemDTO(
                    rst.getString(1),  // Item Id
                    rst.getString(2),  // Category Id
                    rst.getString(3),  // Name
                    rst.getString(4),  // Brand
                    rst.getString(5),  // Model
                    rst.getInt(6),     // Qty
                    rst.getInt(7),     // ReOrder Level
                    rst.getDouble(8)   // Unit Price
            );
            itemDTOS.add(itemDTO);
        }
        return itemDTOS;
    }

    public String getNextItemId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select itemId from item order by itemId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1); // Last item ID
            String substring = lastId.substring(1); // Extract the numeric part
            int i = Integer.parseInt(substring); // Convert the numeric part to integer
            int newIdIndex = i + 1; // Increment the number by 1
            return String.format("I%03d", newIdIndex); // Return the new item ID in format Innn
        }
        return "I001";
    }

    public boolean saveItem(ItemDTO itemDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into item values (?,?,?,?,?,?,?,?)",
                itemDTO.getItemId(),
                itemDTO.getCategoryId(),
                itemDTO.getName(),
                itemDTO.getBrand(),
                itemDTO.getModel(),
                itemDTO.getQtyOnHand(),
                itemDTO.getReOrderLevel(),
                itemDTO.getUnitPrice()
        );
    }

    public boolean updateItem(ItemDTO itemDTO) throws SQLException {
        return CrudUtil.execute(
                "update item set categoryId=?, name=?, brand=?, model=?, qtyOnHand=?, reOrderLevel=?, unitPrice=? where itemId=?",
                itemDTO.getCategoryId(),
                itemDTO.getName(),
                itemDTO.getBrand(),
                itemDTO.getModel(),
                itemDTO.getQtyOnHand(),
                itemDTO.getReOrderLevel(),
                itemDTO.getUnitPrice(),
                itemDTO.getItemId()
        );

    }

    public boolean deleteItem(String itemId) throws SQLException {
        return CrudUtil.execute("delete from item where itemId=?", itemId);
    }

    public ArrayList<String> getAllItemIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select itemId from item");

        ArrayList<String> itemIds = new ArrayList<>();

        while (rst.next()) {
            itemIds.add(rst.getString(1));
        }

        return itemIds;
    }

    public ItemDTO findById(String selectedItemId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from item where itemId=?", selectedItemId);

        if (rst.next()) {
            return new ItemDTO(
                    rst.getString(1),  // Item Id
                    rst.getString(2),  // Category Id
                    rst.getString(3),  // Name
                    rst.getString(4),  // Brand
                    rst.getString(5),  // Model
                    rst.getInt(6),     // Qty
                    rst.getInt(7),     // ReOrder Level
                    rst.getDouble(8)   // Unit Price
            );
        }

        return null;
    }

    public boolean reduceQty(OrderDetailDTO orderDetailsDTO) throws SQLException {
        return CrudUtil.execute(
                "update item set qtyOnHand = qtyOnHand - ? where itemId = ?",
                orderDetailsDTO.getQuantity(),   // Quantity to reduce
                orderDetailsDTO.getItemId()      // Item ID
        );
    }

    public ArrayList<ItemDTO> checkLowStockItems() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from item where qtyOnHand <= reOrderLevel");
        ArrayList<ItemDTO> itemDTOS = new ArrayList<>();

        while (rst.next()) {
            ItemDTO itemDTO = new ItemDTO(
                    rst.getString(1),  // Item Id
                    rst.getString(2),  // Category Id
                    rst.getString(3),  // Name
                    rst.getString(4),  // Brand
                    rst.getString(5),  // Model
                    rst.getInt(6),     // Qty
                    rst.getInt(7),     // ReOrder Level
                    rst.getDouble(8)   // Unit Price
            );
            itemDTOS.add(itemDTO);
        }
        return itemDTOS;
    }

    public boolean increaseQty(OrderDetailDTO orderDetailsDTO) throws SQLException {
        return CrudUtil.execute(
                "UPDATE item SET qtyOnHand = qtyOnHand + ? WHERE itemId = ?",
                orderDetailsDTO.getQuantity(),
                orderDetailsDTO.getItemId()
        );
    }

    public ArrayList<String> getAllItemIdsInCategory(String selectedCategory) throws SQLException {
        ResultSet rst = CrudUtil.execute("select itemId from item where categoryId=?", selectedCategory);

        ArrayList<String> itemIds = new ArrayList<>();

        while (rst.next()) {
            itemIds.add(rst.getString(1));
        }

        return itemIds;
    }

    public boolean increaseQty(PurchaseDetailDTO purchaseDetailDTO) throws SQLException {
        return CrudUtil.execute(
                "UPDATE item SET qtyOnHand = qtyOnHand + ? WHERE itemId = ?",
                purchaseDetailDTO.getQuantity(),
                purchaseDetailDTO.getItemId()
        );
    }

    public String confirmItemCategory(String selectedItemId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select categoryId from item where itemId=?", selectedItemId);

        if (rst.next()) {
            return rst.getString("categoryId");
        }
        return "";
    }

    public boolean reduceQty(PurchaseDetailDTO purchaseDetailDTO) throws SQLException {
        return CrudUtil.execute(
                "update item set qtyOnHand = qtyOnHand - ? where itemId = ?",
                purchaseDetailDTO.getQuantity(),   // Quantity to reduce
                purchaseDetailDTO.getItemId()      // Item ID
        );
    }

    public boolean isEmpty() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select itemId from item order by itemId asc limit 1");
        if(resultSet.next()){
            return false;
        }
        return true;
    }
}
