package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.db.DBConnection;
import lk.ijse.gdse71.mobilezone.dto.OrderReturnDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersReturnModel {
    private final OrderDetailModel orderDetailsModel = new OrderDetailModel();
    public String getNextReturnId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select orderRet_Id from orderRet order by orderRet_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(2);
            int i = Integer.parseInt(substring); // 2
            int newIdIndex = i + 1; // 3
            return String.format("OR%03d", newIdIndex);
        }
        return "OR001";
    }

    public boolean saveReturn(OrderReturnDTO orderReturnDTO) throws SQLException {
        // @connection: Retrieves the current connection instance for the database
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            // @autoCommit: Disables auto-commit to manually control the transaction
            connection.setAutoCommit(false); // 1

            // @isOrderSaved: Saves the order details into the orders table
            boolean isReturnSaved = CrudUtil.execute(
                    "insert into orderRet values (?,?,?,?,?,?,?,?)",
                    orderReturnDTO.getOrderRet_Id(),
                    orderReturnDTO.getOrderId(),
                    orderReturnDTO.getCustomerId(),
                    orderReturnDTO.getItemId(),
                    orderReturnDTO.getQuantity(),
                    orderReturnDTO.getReason(),
                    orderReturnDTO.getRetAmount(),
                    orderReturnDTO.getReturnDate()
            );
            // If the return is saved successfully
            if (isReturnSaved) {

                // @isOrderDetailListSaved: Saves the list of order details
                boolean isOrderDetailListSaved = orderDetailsModel.saveReturnDetailsList(orderReturnDTO.getOrderDetailsDTOS());
                if (isOrderDetailListSaved) {
                    // @commit: Commits the transaction if both order and details are saved successfully
                    connection.commit(); // 2
                    return true;
                }
            }
            // @rollback: Rolls back the transaction if order details saving fails
            connection.rollback(); // 3
            return false;
        } catch (Exception e) {
            // @catch: Rolls back the transaction in case of any exception
            connection.rollback();
            return false;
        } finally {
            // @finally: Resets auto-commit to true after the operation
            connection.setAutoCommit(true); // 4
        }
    }
}
