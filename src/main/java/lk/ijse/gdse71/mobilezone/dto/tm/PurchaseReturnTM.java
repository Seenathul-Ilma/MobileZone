package lk.ijse.gdse71.mobilezone.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseReturnTM {
    private String categoryId;
    private String itemId;
    private String itemName;
    private int returnQty;
    private double unitPrice;
    private double totalAmount;
    private Button cancelPurchaseReturnBtn;
}
