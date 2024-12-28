package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExpenseDTO {
    private String exp_Id;
    private String description;
    private double amount;
    private Date date;
    private String expCategory;
}
