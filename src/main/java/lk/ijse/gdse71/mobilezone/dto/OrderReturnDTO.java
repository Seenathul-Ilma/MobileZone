package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderReturnDTO {
    private String orderRet_Id;
    private String orderId;
    private String customerId;
    private String itemId;
    private int quantity;
    private String reason;
    private double retAmount;
    private Date returnDate;

    private ArrayList<OrderDetailDTO> orderDetailsDTOS;
}
