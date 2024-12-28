package lk.ijse.gdse71.mobilezone.dto.tm;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeTM {
    private String employeeId;
    private String userId;
    private String name;
    private String nic;
    private String designation;
    private String email;
    private String contact;
    private String salary;
    private String compensation;
    private String totalSalary;
}
