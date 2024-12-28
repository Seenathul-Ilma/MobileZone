package lk.ijse.gdse71.mobilezone.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseTM {
    private String categoryId;
    private String itemId;
    private String itemName;
    private int purchaseQty;
    private double purchaseUnitPrice;
    private double totalAmount;
    private Button cancelPurchaseBtn;
}
