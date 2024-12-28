package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseDetailDTO {
    private String purchaseId;
    private String itemId;
    private int quantity;
    private String isReturned;
    private double unitPrice;
    private double totalPrice;
}
