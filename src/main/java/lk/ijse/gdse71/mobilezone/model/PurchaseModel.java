package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.db.DBConnection;
import lk.ijse.gdse71.mobilezone.dto.OrderDTO;
import lk.ijse.gdse71.mobilezone.dto.PurchaseDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseModel {
    private final PurchaseDetailModel purchaseDetailModel = new PurchaseDetailModel();
    public String getNextPurchaseId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select purchaseId from purchase order by purchaseId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring); // 2
            int newIdIndex = i + 1; // 3
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }

    public boolean savePurchase(PurchaseDTO purchaseDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false); // 1

            boolean isPurchaseSaved = CrudUtil.execute(
                    "insert into purchase values (?,?,?,?)",
                    purchaseDTO.getPurchaseId(),
                    purchaseDTO.getSupplierId(),
                    purchaseDTO.getPurchaseDate(),
                    purchaseDTO.getTotalAmount()
                    //orderDTO.getOrderDetailsDTOS()
            );
            if (isPurchaseSaved) {
                boolean isPurchaseDetailListSaved = purchaseDetailModel.savePurchaseDetailsList(purchaseDTO.getPurchaseDetailDTOS());
                if (isPurchaseDetailListSaved) {
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

    public ArrayList<String> getAllPurchaseIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select purchaseId from purchase");

        ArrayList<String> purchaseIds = new ArrayList<>();

        while (rst.next()) {
            purchaseIds.add(rst.getString(1));
        }
        return purchaseIds;
    }

    public PurchaseDTO checkById(String purchaseId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from purchase where purchaseId=?", purchaseId);

        ArrayList<PurchaseDTO> purchaseDTOS = new ArrayList<>();
        if (rst.next()) {
            return new PurchaseDTO(
                    rst.getString(1),  // Order ID
                    rst.getString(2),  // Customer Name
                    rst.getDate(3),  // Order Date
                    rst.getDouble(4)
            );
        }
        return null;
    }
}
