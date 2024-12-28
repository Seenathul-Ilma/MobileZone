package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.db.DBConnection;
import lk.ijse.gdse71.mobilezone.dto.CustomerDTO;
import lk.ijse.gdse71.mobilezone.dto.ItemDTO;
import lk.ijse.gdse71.mobilezone.dto.OrderDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersModel {
    private final OrderDetailModel orderDetailModel = new OrderDetailModel();
    public String getNextOrderId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select orderId from orders order by orderId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring); // 2
            int newIdIndex = i + 1; // 3
            return String.format("O%03d", newIdIndex);
        }
        return "O001";
    }

    public boolean saveOrder(OrderDTO orderDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false); // 1

            boolean isOrderSaved = CrudUtil.execute(
                    "insert into orders values (?,?,?,?)",
                    orderDTO.getOrderId(),
                    orderDTO.getCustomerId(),
                    orderDTO.getOrderDate(),
                    orderDTO.getEmployeeId()
                    //orderDTO.getOrderDetailsDTOS()
            );
            if (isOrderSaved) {
                boolean isOrderDetailListSaved = orderDetailModel.saveOrderDetailsList(orderDTO.getOrderDetailsDTOS());
                if (isOrderDetailListSaved) {
                    connection.commit(); // 2
                    return true;
                }
            }
            connection.rollback(); // 3
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true); // 4
        }
    }

    public ArrayList<String> getAllOrderIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select orderId from orders");

        ArrayList<String> orderIds = new ArrayList<>();

        while (rst.next()) {
            orderIds.add(rst.getString(1));
        }
        return orderIds;
    }

    public OrderDTO checkById(String orderId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from orders where orderId=?", orderId);

        ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
        if (rst.next()) {
            return new OrderDTO(
                    rst.getString(1),  // Order ID
                    rst.getString(2),  // Customer Name
                    rst.getDate(3),  // Order Date
                    rst.getString(4)
            );
        }
        return null;
    }
}
