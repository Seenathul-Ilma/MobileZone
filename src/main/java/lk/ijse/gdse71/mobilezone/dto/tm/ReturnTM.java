package lk.ijse.gdse71.mobilezone.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReturnTM {
    private String itemId;
    private String itemName;
    private int returnQty;
    private double soldPrice;
    private double totalAmount;
    private Button cancelReturnBtn;
}
