package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseReturnDTO {
    private String purRet_Id;
    private String purchaseId;
    private String supplierId;
    private String itemId;
    private int retQuantity;
    private String reason;
    private double retAmount;
    private Date returnDate;

    private ArrayList<PurchaseDetailDTO> purchaseDetailDTOS;
}
