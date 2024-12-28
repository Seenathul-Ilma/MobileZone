package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseDTO {
    private String purchaseId;
    private String supplierId;
    private Date purchaseDate;
    private double totalAmount;
    private ArrayList<PurchaseDetailDTO> purchaseDetailDTOS;

    public PurchaseDTO(String purchaseId, String supplierId, Date purchaseDate, double totalAmount) {
        this.purchaseId = purchaseId;
        this.supplierId = supplierId;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
    }
}
