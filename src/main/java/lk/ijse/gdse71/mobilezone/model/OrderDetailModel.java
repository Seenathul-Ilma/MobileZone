package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.dto.OrderDTO;
import lk.ijse.gdse71.mobilezone.dto.OrderDetailDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailModel {
    private final ItemModel itemModel = new ItemModel();
    public boolean saveOrderDetailsList(ArrayList<OrderDetailDTO> orderDetailsDTOS) throws SQLException {
        for (OrderDetailDTO orderDetailsDTO : orderDetailsDTOS) {
            boolean isOrderDetailsSaved = saveOrderDetail(orderDetailsDTO);
            if (!isOrderDetailsSaved) {
                return false;
            }

            // @isItemUpdated: Updates the item quantity in the stock for the corresponding order detail
            boolean isItemUpdated = itemModel.reduceQty(orderDetailsDTO);
            if (!isItemUpdated) {
                // Return false if updating the item quantity fails
                return false;
            }
        }
        // Return true if all order details are saved and item quantities updated successfully
        return true;
    }

    private boolean saveOrderDetail(OrderDetailDTO orderDetailsDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into orderDetail values (?,?,?,?,?)",
                orderDetailsDTO.getOrderId(),
                orderDetailsDTO.getItemId(),
                orderDetailsDTO.getQuantity(),
                orderDetailsDTO.getIsReturned(),
                orderDetailsDTO.getUnitPrice()
        );
    }

    public OrderDetailDTO findById(String selectedOrderId, String selectedItemId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from orderDetail where orderId=? AND itemId=?", selectedOrderId, selectedItemId);

        if (rst.next()) {
            return new OrderDetailDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getDouble(5)
            );
        }else{
            return null;
        }
    }

    public boolean saveReturnDetailsList(ArrayList<OrderDetailDTO> orderDetailsDTOS) throws SQLException {
        for (OrderDetailDTO orderDetailsDTO : orderDetailsDTOS) {
            boolean isReturnDetailsSaved = updateOrderDetail(orderDetailsDTO);
            if (!isReturnDetailsSaved) {
                return false;
            }

            // @isItemUpdated: Updates the item quantity in the stock for the corresponding order detail
            boolean isItemUpdated = itemModel.increaseQty(orderDetailsDTO);
            if (!isItemUpdated) {
                // Return false if updating the item quantity fails
                return false;
            }
        }
        return true;
    }

    private boolean updateOrderDetail(OrderDetailDTO orderDetailsDTO) throws SQLException {
        return CrudUtil.execute(
                "update orderDetail set isReturned=? where orderId=?",
                orderDetailsDTO.getIsReturned(),
                orderDetailsDTO.getOrderId()
        );
    }

    public String getReturnOrNot(String orderId, String itemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select isReturned from orderDetail where orderId=? AND itemId=?", orderId, itemId);
        if(resultSet.next()){
            return resultSet.getString("isReturned");
        }
        return "NO";
    }

    public String confirmItemBought(String selectedOrderId, String selectedItemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select itemId from orderDetail where orderId=? AND itemId=?", selectedOrderId, selectedItemId);
        if(resultSet.next()){
            return resultSet.getString("itemId");
        }
        return "";
    }

    public String confirmCustomerBought(String selectedCustomerId, String selectedOrderId, String selectedItemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute(
                "select customerId from orders o join orderDetail od on o.orderId = od.orderId where customerId = ? AND itemId=? AND od.orderId=? ",
                selectedCustomerId, selectedItemId, selectedOrderId);
        if(resultSet.next()){
            return resultSet.getString("customerId");
        }
        return "";
    }
}
