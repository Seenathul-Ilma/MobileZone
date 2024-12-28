package lk.ijse.gdse71.mobilezone.model;

import lk.ijse.gdse71.mobilezone.dto.OrderDetailDTO;
import lk.ijse.gdse71.mobilezone.dto.PurchaseDetailDTO;
import lk.ijse.gdse71.mobilezone.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaseDetailModel {
    private final ItemModel itemModel = new ItemModel();
    public boolean savePurchaseDetailsList(ArrayList<PurchaseDetailDTO> purchaseDetailDTOS) throws SQLException {
        for (PurchaseDetailDTO purchaseDetailDTO : purchaseDetailDTOS) {
            boolean isPurchaseDetailsSaved = savePurchaseDetail(purchaseDetailDTO);
            if (!isPurchaseDetailsSaved) {
                return false;
            }

            boolean isItemUpdated = itemModel.increaseQty(purchaseDetailDTO);
            if (!isItemUpdated) {
                // Return false if updating the item quantity fails
                return false;
            }
        }
        return true;
    }

    private boolean savePurchaseDetail(PurchaseDetailDTO purchaseDetailDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into purchaseDetail values (?,?,?,?,?,?)",
                purchaseDetailDTO.getPurchaseId(),
                purchaseDetailDTO.getItemId(),
                purchaseDetailDTO.getQuantity(),
                purchaseDetailDTO.getIsReturned(),
                purchaseDetailDTO.getUnitPrice(),
                purchaseDetailDTO.getTotalPrice()
        );
    }

    public PurchaseDetailDTO findById(String purchaseId, String selectedItemId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from purchaseDetail where purchaseId=? AND itemId=?", purchaseId, selectedItemId);

        if (rst.next()) {
            return new PurchaseDetailDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getDouble(6)
            );
        }else{
            return null;
        }
    }

    public String getReturnOrNot(String purchaseId, String selectedItemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select isReturned from purchaseDetail where purchaseId=? AND itemId=?", purchaseId, selectedItemId);
        if(resultSet.next()){
            return resultSet.getString("isReturned");
        }
        return "NO";
    }

    public String confirmItemBought(String selectedPurchaseId, String selectedItemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select itemId from purchaseDetail where purchaseId=? AND itemId=?", selectedPurchaseId, selectedItemId);
        if(resultSet.next()){
            return resultSet.getString("itemId");
        }
        return "";
    }

    public String confirmBoughtFromSupplier(String selectedSupplierId, String selectedPurchaseId, String selectedItemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute(
                "select supplierId from purchase p join purchaseDetail pd on p.purchaseId = pd.purchaseId where supplierId = ? AND itemId=? AND pd.purchaseId=? ",
                selectedSupplierId, selectedItemId, selectedPurchaseId);
        if(resultSet.next()){
            return resultSet.getString("supplierId");
        }
        return "";
    }

    public boolean saveReturnDetailsList(ArrayList<PurchaseDetailDTO> purchaseDetailDTOS) throws SQLException {
        for (PurchaseDetailDTO purchaseDetailDTO : purchaseDetailDTOS) {
            boolean isReturnDetailsSaved = updatePurchaseDetail(purchaseDetailDTO);
            if (!isReturnDetailsSaved) {
                return false;
            }

            boolean isItemUpdated = itemModel.reduceQty(purchaseDetailDTO);
            if (!isItemUpdated) {
                // Return false if updating the item quantity fails
                return false;
            }
        }
        return true;
    }

    private boolean updatePurchaseDetail(PurchaseDetailDTO purchaseDetailDTO) throws SQLException {
        return CrudUtil.execute(
                "update purchaseDetail set isReturned=? where purchaseId=?",
                purchaseDetailDTO.getIsReturned(),
                purchaseDetailDTO.getPurchaseId()
        );
    }
}
