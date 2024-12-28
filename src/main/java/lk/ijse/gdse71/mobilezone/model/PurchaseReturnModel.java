package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.db.DBConnection;
import lk.ijse.gdse71.mobilezone.dto.PurchaseReturnDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseReturnModel {
    private final PurchaseDetailModel purchaseDetailModel = new PurchaseDetailModel();
    public String getNextPurchaseReturnId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select purRet_Id from purReturn order by purRet_Id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(2);
            int i = Integer.parseInt(substring); // 2
            int newIdIndex = i + 1; // 3
            return String.format("PR%03d", newIdIndex);
        }
        return "PR001";
    }

    public boolean saveReturn(PurchaseReturnDTO purchaseReturnDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false); // 1

            boolean isReturnSaved = CrudUtil.execute(
                    "insert into purReturn values (?,?,?,?,?,?,?,?)",
                    purchaseReturnDTO.getPurRet_Id(),
                    purchaseReturnDTO.getPurchaseId(),
                    purchaseReturnDTO.getSupplierId(),
                    purchaseReturnDTO.getItemId(),
                    purchaseReturnDTO.getRetQuantity(),
                    purchaseReturnDTO.getReason(),
                    purchaseReturnDTO.getRetAmount(),
                    purchaseReturnDTO.getReturnDate()
            );

            if (isReturnSaved) {
                boolean isPurchaseDetailListSaved = purchaseDetailModel.saveReturnDetailsList(purchaseReturnDTO.getPurchaseDetailDTOS());
                if (isPurchaseDetailListSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
