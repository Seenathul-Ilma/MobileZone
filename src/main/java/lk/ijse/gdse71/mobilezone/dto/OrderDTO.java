package lk.ijse.gdse71.mobilezone.dto;


import lombok.*;

import java.util.ArrayList;
import java.sql.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {
    private String orderId;
    private String customerId;
    private Date orderDate;
    private String employeeId;

    private ArrayList<OrderDetailDTO> orderDetailsDTOS;

    public OrderDTO(String orderId, String customerId, Date orderDate, String employeeId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.employeeId = employeeId;
    }
}
