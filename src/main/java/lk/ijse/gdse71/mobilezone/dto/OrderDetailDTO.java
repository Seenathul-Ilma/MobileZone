package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

import java.util.ArrayList;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private String orderId;
    private String itemId;
    private int quantity;
    private String isReturned;
    private double unitPrice;
}
