package lk.ijse.gdse71.mobilezone.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierDTO {
    private String supplierId;
    private String companyName;
    private String contactPerson;
    private String nic;
    private String address;
    private String email;
    private String phone;
}
