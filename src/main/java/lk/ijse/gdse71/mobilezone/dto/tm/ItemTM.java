package lk.ijse.gdse71.mobilezone.dto.tm;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemTM {
    private String itemId;
    private String categoryId;
    private String name;
    private String brand;
    private String model;
    private int qtyOnHand;
    private int reOrderLevel;
    private double unitPrice;

}
